document.addEventListener('DOMContentLoaded', function() {
    // Sélectionne toutes les inputs de type 'checkbox' avec le data-field 'holiday'
    const holidayCheckboxes = document.querySelectorAll('input[data-field="holiday"]');

    holidayCheckboxes.forEach(checkbox => {
        const userId = checkbox.dataset.userId;
        const endOfHolidayInput = document.getElementById(`endOfHoliday-${userId}`);
        const holidayMessageId = `holiday-message-${userId}`; // ID unique pour le message d'avertissement

        /**
         * Gère la visibilité de l'input de fin de vacances et initialise sa valeur si nécessaire.
         * Cette fonction est appelée au chargement de la page et lors du changement de la checkbox "Vacances".
         */
        function toggleEndOfHolidayVisibility() {
            if (checkbox.checked) {
                // Si la checkbox "Vacances" est cochée, affiche l'input de date
                endOfHolidayInput.style.display = 'inline-block'; // Ou 'block' selon votre style CSS
                // La valeur de l'input est déjà définie par Thymeleaf via th:value.
                // On n'a plus besoin de la récupérer de th:data et de la définir manuellement ici.
                displayHolidayMessage(); // Affiche le message d'avertissement si la date est vide
            } else {
                // Si la checkbox "Vacances" est décochée, masque l'input de date
                endOfHolidayInput.style.display = 'none';
                endOfHolidayInput.value = ''; // Vide la valeur de l'input de date (important pour la persistance côté serveur)
                removeHolidayMessage(); // Supprime le message d'avertissement
            }
        }

        /**
         * Affiche un message d'avertissement si la checkbox "Vacances" est cochée mais que la date est vide.
         */
        function displayHolidayMessage() {
            let messageElement = document.getElementById(holidayMessageId);
            // Vérifie l'état actuel de la checkbox et si la valeur du champ de date est vide
            if (checkbox.checked && !endOfHolidayInput.value) {
                if (!messageElement) {
                    // Crée l'élément du message s'il n'existe pas
                    messageElement = document.createElement('div');
                    messageElement.id = holidayMessageId;
                    messageElement.className = 'text-danger mt-1'; // Applique des classes Bootstrap pour le style
                    checkbox.parentNode.appendChild(messageElement); // Ajoute le message après la checkbox
                }
                messageElement.textContent = 'Les modifications ne seront pas sauvegardées tant que la date de fin de vacances n\'est pas renseignée.';
            } else {
                // Si les conditions ne sont plus remplies, retire le message
                removeHolidayMessage();
            }
        }

        /**
         * Supprime le message d'avertissement.
         */
        function removeHolidayMessage() {
            const messageElement = document.getElementById(holidayMessageId);
            if (messageElement) {
                messageElement.remove();
            }
        }

        // --- Écouteurs d'événements ---

        // Écoute les changements sur la checkbox "Vacances"
        checkbox.addEventListener('change', function() {
            toggleEndOfHolidayVisibility(); // Met à jour l'affichage de l'input de date

            // Logique pour déclencher l'appel AJAX
            if (this.checked && !endOfHolidayInput.value) {
                // Si la case est cochée MAIS la date est vide, n'envoie PAS d'appel AJAX.
                // Affiche seulement le message d'avertissement.
                displayHolidayMessage();
            } else {
                // Dans tous les autres cas (case cochée + date renseignée, ou case décochée) :
                // Lance l'appel AJAX pour mettre à jour le statut des vacances et la date.
                // `this.checked` est la nouvelle valeur de la checkbox (true/false).
                // `endOfHolidayInput.value || null` assure que null est envoyé si le champ de date est vide.
                updateHolidayStatus(userId, this.checked, endOfHolidayInput.value || null, this, endOfHolidayInput);
            }
        });

        // Écoute les changements sur l'input de date de fin de vacances
        endOfHolidayInput.addEventListener('change', function() {
            displayHolidayMessage(); // Met à jour le message d'avertissement (disparaît si la date est valide)

            const selectedDate = this.value; // Récupère la date sélectionnée (format YYYY-MM-DD)
            const today = new Date().toISOString().slice(0, 10); // Date du jour au format YYYY-MM-DD

            if (selectedDate && selectedDate < today) { // Si une date est sélectionnée et qu'elle est passée
                alert("La date de fin de vacances ne peut pas être dans le passé. Le champ a été vidé et la case décochée.");
                this.value = ''; // Vide l'input de date
                checkbox.checked = false; // Décoche la checkbox
                toggleEndOfHolidayVisibility(); // Met à jour la visibilité de l'input de date (le cachera)
                // Pas d'appel AJAX ici car la donnée est invalide côté client
            } else if (checkbox.checked) {
                // `this.value || null` assure que null est envoyé si le champ est vidé manuellement.
                updateHolidayStatus(userId, checkbox.checked, this.value || null, checkbox, this);
            }
        });

        // Appelle la fonction de visibilité au chargement de la page pour initialiser l'état
        // Cela assure que l'input de date est visible ou caché correctement dès le début.
        toggleEndOfHolidayVisibility();
    });
});

/**
 * Effectue un appel AJAX pour mettre à jour le statut des vacances et la date de fin.
 * @param {string} userId - L'ID de l'utilisateur.
 * @param {boolean} holidayValue - La nouvelle valeur du statut "Vacances" (true/false).
 * @param {string|null} endOfHolidayValue - La date de fin de vacances (format YYYY-MM-DD) ou null.
 * @param {HTMLInputElement} holidayCheckboxElement - L'élément DOM de la checkbox "Vacances".
 * @param {HTMLInputElement} endOfHolidayInputElement - L'élément DOM de l'input de date de fin de vacances.
 */
function updateHolidayStatus(userId, holidayValue, endOfHolidayValue, holidayCheckboxElement, endOfHolidayInputElement) {
    const dataToSend = {
        userId: userId,
        fieldName: 'holiday', // Indique quel champ est principalement concerné par cette action
        newValue: holidayValue, // Le statut de la checkbox
        endOfHoliday: endOfHolidayValue // La date associée
    };

    fetch(`/updateHolidayData`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            // Récupère le jeton CSRF pour la sécurité Spring Security
            'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content
        },
        body: JSON.stringify(dataToSend) // Convertit l'objet JavaScript en chaîne JSON
    })
        .then(response => {
            // Vérifie si la réponse HTTP est dans la plage 200-299 (succès)
            if (!response.ok) {
                console.error('Erreur lors de la mise à jour des vacances:', response.statusText);
                alert('Erreur lors de l\'enregistrement des vacances. Veuillez réessayer.');

                // Revertit l'état des éléments du formulaire en cas d'échec
                if (holidayCheckboxElement) {
                    holidayCheckboxElement.checked = !holidayValue; // Rétablit la checkbox à son état précédent
                }
                if (endOfHolidayInputElement && endOfHolidayValue !== null) {
                    // Ici, vous pouvez choisir de restaurer la date précédente ou de la vider.
                    // Par défaut, nous ne la touchons pas ici pour éviter des complications.
                }
            } else {
                console.log('Mise à jour des vacances réussie !');
                // Optionnel : Recharger la page après une mise à jour réussie.
                // Décommentez la ligne ci-dessous si vous préférez un rechargement complet.
                // window.location.reload();
            }
        })
        .catch(error => {
            // Gère les erreurs réseau ou autres exceptions lors de l'appel fetch
            console.error('Erreur réseau ou autre lors de la mise à jour des vacances:', error);
            alert('Erreur de connexion. Veuillez vérifier votre réseau.');

            // Revertit l'état des éléments du formulaire en cas d'erreur réseau
            if (holidayCheckboxElement) {
                holidayCheckboxElement.checked = !holidayValue;
            }
            if (endOfHolidayInputElement && endOfHolidayValue !== null) {
                // Idem, choisir de restaurer ou non la date
            }
        });
}

/**
 * Fonction pour la mise à jour des autres checkboxes (quota, warning, etc.).
 * Elle est appelée via l'attribut onchange directement dans le HTML.
 * @param {HTMLInputElement} checkbox - L'élément DOM de la checkbox qui a été modifiée.
 */
function updateCheckboxValue(checkbox) {
    const userId = checkbox.dataset.userId;
    const fieldName = checkbox.dataset.field;
    const newValue = checkbox.checked; // La nouvelle valeur de la checkbox (true/false)

    fetch(`/updateBoolean`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content // Jeton CSRF
        },
        // Envoie l'ID de l'utilisateur, le nom du champ et la nouvelle valeur
        body: JSON.stringify({
            userId: userId,
            fieldName: fieldName,
            newValue: newValue
        })
    })
        .then(response => {
            if (!response.ok) {
                console.error('Erreur lors de la mise à jour:', response.statusText);
                checkbox.checked = !newValue; // Revenir à l'état précédent en cas d'erreur
                alert('Erreur lors de l\'enregistrement. Veuillez réessayer.');
            } else {
                console.log('Mise à jour réussie !');
                // Recharger la page pour refléter les changements (cela peut être optimisé pour une UX plus fluide)
                window.location.reload();
            }
        })
        .catch(error => {
            console.error('Erreur réseau ou autre:', error);
            checkbox.checked = !newValue; // Revenir à l'état précédent en cas d'erreur réseau
            alert('Erreur de connexion. Veuillez vérifier votre réseau.');
        });
}