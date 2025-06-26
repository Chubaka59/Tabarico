document.addEventListener("DOMContentLoaded", function () {
    showStock(document.getElementById("product"));
    setDefaultDateAndPersist()
})

function showStock(select) {
    const selectedProduct = select.options[select.selectedIndex];
    const actualStockSpan = document.getElementById("actualStock");

    actualStockSpan.textContent = selectedProduct.getAttribute("stock");
}

function refreshPageWithDate() {
    var dateInput = document.getElementById('date');
    var selectedDate = dateInput.value; // Obtient la valeur de la date au format 'YYYY-MM-DD'

    // Vérifie si une date a été sélectionnée
    if (selectedDate) {
        // Construit l'URL actuelle
        var currentUrl = window.location.href;
        var url = new URL(currentUrl);

        // Met à jour ou ajoute le paramètre 'date'
        url.searchParams.set('date', selectedDate);

        // Recharge la page avec la nouvelle URL
        window.location.href = url.toString();
    }
}

function setDefaultDateAndPersist() {
    var dateInput = document.getElementById('date');
    var urlParams = new URLSearchParams(window.location.search);
    var dateFromUrl = urlParams.get('date');

    if (dateFromUrl) {
        // Si une date est présente dans l'URL, l'utiliser
        dateInput.value = dateFromUrl;
    } else {
        // Sinon, définir la date d'aujourd'hui comme valeur par défaut
        var today = new Date();
        var year = today.getFullYear();
        var month = (today.getMonth() + 1).toString().padStart(2, '0'); // Mois commence à 0
        var day = today.getDate().toString().padStart(2, '0');
        dateInput.value = `${year}-${month}-${day}`;
    }

    // Ajouter un écouteur d'événements 'change' pour rafraîchir la page lorsque la date est modifiée
    dateInput.addEventListener('change', refreshPageWithDate);
}