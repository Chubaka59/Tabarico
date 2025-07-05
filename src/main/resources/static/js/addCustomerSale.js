let isCalculatingByPrice = false; // Global variable to track the calculation mode

function toggleCalculationFields() {
    const calculateByPriceCheckbox = document.getElementById('calculateByPrice');
    const quantityInput = document.getElementById('quantity');
    const totalPriceInput = document.getElementById('totalPrice');

    isCalculatingByPrice = calculateByPriceCheckbox.checked;

    if (isCalculatingByPrice) {
        quantityInput.setAttribute('disabled', 'true'); // Disable quantity
        quantityInput.value = ''; // Clear quantity value
        totalPriceInput.removeAttribute('disabled'); // Enable total price
        totalPriceInput.focus(); // Set focus on total price
    } else {
        totalPriceInput.setAttribute('disabled', 'true'); // Disable total price
        totalPriceInput.value = ''; // Clear total price value
        quantityInput.removeAttribute('disabled'); // Enable quantity
        quantityInput.focus(); // Set focus on quantity
    }
    calculate(); // Perform initial calculation after mode change
}

function getPricePerUnit() {
    const productSelect = document.getElementById('product');
    const typeOfSaleSelect = document.getElementById('typeOfSale');
    const contractSelect = document.getElementById('contract');

    const productSelectedOption = productSelect.options[productSelect.selectedIndex];
    const contractSelectedOption = contractSelect.options[contractSelect.selectedIndex];

    let pricePerUnit = 0;

    if (typeOfSaleSelect.options[typeOfSaleSelect.selectedIndex].value === "cleanMoney") {
        // Keep price per unit as float, no rounding here
        pricePerUnit = parseFloat(productSelectedOption.getAttribute("cleanPrice"));
        if (contractSelect.options[contractSelect.selectedIndex].value !== "") {
            const reduction = parseFloat(contractSelectedOption.getAttribute("reduction"));
            // Apply reduction, keep as float
            pricePerUnit = pricePerUnit * (1 - reduction / 100);
        }
    } else {
        // Keep price per unit as float
        pricePerUnit = parseFloat(productSelectedOption.getAttribute("dirtyPrice"));
    }
    return pricePerUnit;
}

function calculate() {
    const quantityInput = document.getElementById('quantity');
    const totalPriceInput = document.getElementById('totalPrice');

    const pricePerUnit = getPricePerUnit();

    if (isCalculatingByPrice) { // Mode: calculate quantity from total price
        const totalPriceEntered = parseFloat(totalPriceInput.value);
        if (!isNaN(totalPriceEntered) && totalPriceEntered >= 0 && pricePerUnit > 0) {
            const rawCalculatedQuantity = totalPriceEntered / pricePerUnit;
            const finalQuantity = Math.floor(rawCalculatedQuantity); // Round quantity down

            quantityInput.value = finalQuantity; // Update quantity

            // Recalculate and update total price based on the rounded quantity
            const actualTotalPrice = Math.round(finalQuantity * pricePerUnit); // Round total price to nearest integer
            totalPriceInput.value = actualTotalPrice;
        } else {
            quantityInput.value = '';
            totalPriceInput.value = ''; // Clear total price if input is invalid
        }
    } else { // Mode: calculate total price from quantity
        const quantity = parseFloat(quantityInput.value);
        if (!isNaN(quantity) && quantity >= 0) {
            const rawCalculatedTotalPrice = pricePerUnit * quantity;
            const finalTotalPrice = Math.round(rawCalculatedTotalPrice); // Round total price to nearest integer
            totalPriceInput.value = finalTotalPrice;
        } else {
            totalPriceInput.value = '';
        }
    }
}

// This function is called when a selector (product, sale type, contract) changes.
function updateCalculationMode() {
    const contractSelect = document.getElementById('contract');
    const totalPriceInput = document.getElementById('totalPrice');

    // If we're calculating quantity from price AND the contract changed, clear totalPrice
    // This prevents outdated calculations based on an old pricePerUnit
    if (isCalculatingByPrice && document.activeElement === contractSelect) {
        totalPriceInput.value = ''; // Clear total price input
    }

    calculate(); // Perform calculation based on the current mode
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', () => {
    // Initialiser l'état des champs en fonction de la checkbox (qui est décochée par default)
    toggleCalculationFields();

    // Call calculate() after toggle for an initial calculation if values are present
    calculate();

    // Ensure disabled fields are not submitted
    const form = document.querySelector('form');
    form.addEventListener('submit', (event) => {
        const quantityInput = document.getElementById('quantity');
        // The quantity input must always be enabled to be submitted as it's the DTO field.
        quantityInput.removeAttribute('disabled');
        // No need to explicitly disable totalPriceInput if it's not the target DTO field.
        // It's handled by the 'disabled' attribute based on toggleCalculationFields.
    });
});