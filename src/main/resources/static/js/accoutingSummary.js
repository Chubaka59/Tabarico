function updateCheckboxValue(checkbox) {
    const userId = checkbox.dataset.userId;
    const fieldName = checkbox.dataset.field;
    const newValue = checkbox.checked; // true ou false

    fetch(`/updateBoolean`, {
        method: 'POST', // Ou POST, selon votre préférence RESTful
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content
            // Si vous avez CSRF activé (ce qui est recommandé avec Spring Security),
            // vous devrez inclure le token CSRF ici.
            // Exemple : 'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content
        },
        body: JSON.stringify({
            userId: userId,
            fieldName: fieldName,
            newValue: newValue
        })
    })
        .then(response => {
            if (!response.ok) {
                // Gérer les erreurs (par exemple, afficher un message à l'utilisateur)
                console.error('Erreur lors de la mise à jour:', response.statusText);
                // Optionnel : Revenir à l'état précédent de la checkbox si la mise à jour échoue
                checkbox.checked = !newValue;
                alert('Erreur lors de l\'enregistrement. Veuillez réessayer.');
            } else {
                console.log('Mise à jour réussie !');
                // Optionnel : Afficher un message de succès temporaire
                window.location.reload();
            }
        })
        .catch(error => {
            console.error('Erreur réseau ou autre:', error);
            checkbox.checked = !newValue; // Revenir à l'état précédent
            alert('Erreur de connexion. Veuillez vérifier votre réseau.');
        });
}