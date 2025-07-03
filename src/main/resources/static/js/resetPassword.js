function checkPassword() {
    const password = document.getElementById('password').value;
    const passwordConfirmation = document.getElementById('passwordConfirmation').value;
    const submitButton = document.getElementById('submitButton');
    const passwordMatchMessage = document.getElementById('passwordMatchMessage');

    // Cas 1 : Si les deux champs sont vides, désactiver le bouton et masquer le message.
    // Cela gère l'état initial de la page.
    if (password.trim() === '' && passwordConfirmation.trim() === '') {
        submitButton.disabled = true;
        submitButton.style.opacity = '0.5';
        passwordMatchMessage.style.display = 'none';
        return; // Sortir de la fonction car il n'y a rien à valider pour l'instant.
    }

    // Cas 2 : Si le mot de passe principal est vide (mais la confirmation peut avoir du texte),
    // afficher le message d'erreur spécifique.
    if (password.trim() === '') {
        submitButton.disabled = true;
        submitButton.style.opacity = '0.5';
        passwordMatchMessage.textContent = 'Le mot de passe ne peut pas être vide.';
        passwordMatchMessage.style.display = 'block';
        return; // Sortir.
    }

    // Cas 3 : Si les mots de passe ne correspondent pas.
    if (password !== passwordConfirmation) {
        submitButton.disabled = true;
        submitButton.style.opacity = '0.5';
        passwordMatchMessage.textContent = 'Les mots de passe ne correspondent pas.';
        passwordMatchMessage.style.display = 'block';
    } else {
        // Cas 4 : Les mots de passe correspondent et ne sont pas vides.
        submitButton.disabled = false;
        submitButton.style.opacity = '1';
        passwordMatchMessage.style.display = 'none'; // Cache le message
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const passwordInput = document.getElementById('password');
    const passwordConfirmationInput = document.getElementById('passwordConfirmation');

    if (passwordInput && passwordConfirmationInput) {
        // Nous allons utiliser 'input' au lieu de 'keyup' pour mieux capter les changements
        // y compris les copier-coller, auto-remplissage etc.
        passwordInput.addEventListener('input', checkPassword);
        passwordConfirmationInput.addEventListener('input', checkPassword);
        // Exécutez la validation une fois au chargement de la page pour définir l'état initial du bouton.
        checkPassword();
    }
});