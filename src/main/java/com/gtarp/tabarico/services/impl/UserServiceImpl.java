package com.gtarp.tabarico.services.impl;

import com.gtarp.tabarico.dto.CheckboxUpdateRequestDto;
import com.gtarp.tabarico.dto.UserDto;
import com.gtarp.tabarico.entities.User;
import com.gtarp.tabarico.exception.FileTypeException;
import com.gtarp.tabarico.exception.StorageException;
import com.gtarp.tabarico.exception.UserAlreadyExistException;
import com.gtarp.tabarico.exception.UserNotFoundException;
import com.gtarp.tabarico.repositories.UserRepository;
import com.gtarp.tabarico.services.AbstractCrudService;
import com.gtarp.tabarico.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl extends AbstractCrudService<User, UserRepository, UserDto> implements UserService {

    public UserServiceImpl(UserRepository repository) {
        super(repository);
    }


    @Override
    public User getById(Integer id) {
        return this.repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User insert(UserDto userDto) {
        Optional<User> existingUser = this.repository.findUserByUsername(userDto.getUsername());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistException(userDto.getUsername());
        }
        User newUser = new User(userDto);
        if (userDto.getIdentityCardImage() != null && !userDto.getIdentityCardImage().isEmpty()) {
            newUser.setIdentityCardImage(saveIdentityCardImage(userDto.getIdentityCardImage(), newUser.getUsername()));
        }
        return this.repository.save(newUser);
    }

    @Override
    public User update(Integer id, UserDto userDto) {
        User updatedUser = getById(id).update(userDto);
        if (userDto.getIdentityCardImage() != null && !userDto.getIdentityCardImage().isEmpty()) {
            updatedUser.setIdentityCardImage(saveIdentityCardImage(userDto.getIdentityCardImage(), updatedUser.getUsername()));
        }
        return repository.save(updatedUser);
    }

    public void updateBooleanValue(CheckboxUpdateRequestDto checkboxUpdateRequestDto) {
        User user = getById(checkboxUpdateRequestDto.getUserId());
        switch (checkboxUpdateRequestDto.getFieldName()) {
            case "quota":
                user.setQuota(checkboxUpdateRequestDto.isNewValue());
                break;

            case "exporterQuota":
                user.setExporterQuota(checkboxUpdateRequestDto.isNewValue());
                break;

            case "warning1":
                user.setWarning1(checkboxUpdateRequestDto.isNewValue());
                break;

            case "warning2":
                user.setWarning2(checkboxUpdateRequestDto.isNewValue());
                break;
        }
        this.repository.save(user);
    }

    public User getByUsername(String username) {
        return this.repository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public User updatePassword(Integer id, UserDto userDto) {
        User updatedEntity = getById(id).updatePassword(userDto);
        return repository.save(updatedEntity);
    }

    @Override
    public void updateHoliday(Map<String, Object> payload) {
        Integer userId = Integer.parseInt((String) payload.get("userId"));
        Boolean holidayValue = (Boolean) payload.get("newValue");
        String endOfHolidayStr = (String) payload.get("endOfHoliday");

        LocalDate endOfHoliday = null;
        if (endOfHolidayStr != null && !endOfHolidayStr.isEmpty()) {
            endOfHoliday = LocalDate.parse(endOfHolidayStr);
            if (endOfHoliday.isBefore(LocalDate.now())) {
                endOfHoliday = null;
                holidayValue = false;
            }
        }

        User user = getById(userId);
        user.setHoliday(holidayValue);
        user.setEndOfHoliday(endOfHoliday);
        this.repository.save(user);
    }


    public User disableHolidayWhenExpire(User user) {
        if (user.getEndOfHoliday() != null && user.getEndOfHoliday().isBefore(LocalDate.now())) {
            user.setHoliday(false);
            user.setEndOfHoliday(null);
            return this.repository.save(user);
        }
        return user;
    }

    @Value("${app.upload.dir}")
    private String uploadDir;

    private String saveIdentityCardImage(MultipartFile file, String username) {
        String[] imageFormat = new String[]{"jpeg", "png", "webp"};
        Path destinationFile;
        String fileName;

        if (file == null || file.isEmpty()) {
            throw new StorageException("Failed to store empty file.");
        }
        try {
            if (Arrays.stream(imageFormat).noneMatch(file.getContentType()::contains)) {
                throw new FileTypeException(file.getContentType() + "is not a valid type of file");
            }

            // Générer un nom de fichier lié a l'utilisateur
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.lastIndexOf(".") != -1) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            fileName = "CarteIdentite_" + username + fileExtension;

            Path location = Path.of(uploadDir);

            // Crée le répertoire s'il n'existe pas
            if (!Files.exists(location)) {
                Files.createDirectories(location);
            }

            destinationFile = location.resolve(fileName);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
        return fileName;
    }

    private void deleteIdentityCardImage(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return;
        }

        Path filePath = Paths.get(uploadDir).resolve(fileName);
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            //TODO Logger a créer
        }
    }

    @Override
    public void delete(Integer id) {
        User user = getById(id);
        repository.delete(user);
        deleteIdentityCardImage(user.getIdentityCardImage());
    }
}
