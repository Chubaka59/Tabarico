function calculatePrice(form) {
    const product = form.elements['product'];
    const quantity = form.elements['quantity'].value;
    const typeOfSale = form.elements['typeOfSale'];
    const contract = form.elements['contract'];
    const totalPriceSpan = document.getElementById('totalPrice');
    let pricePerUnit = 0;
    const productSelectedOption = product.options[product.selectedIndex];
    const contractSelectedOption = contract.options[contract.selectedIndex];

    if(typeOfSale.options[typeOfSale.selectedIndex].value === "cleanMoney") {
        pricePerUnit = productSelectedOption.getAttribute("cleanPrice");
        if (contract.options[contract.selectedIndex].value !== "none") {
            pricePerUnit = pricePerUnit - (pricePerUnit * contractSelectedOption.getAttribute("reduction")/100)
        }
    } else {
        pricePerUnit = productSelectedOption.getAttribute("dirtyPrice");
    }

    let totalPrice;
    if(!isNaN(quantity) && quantity > 0) {
        totalPrice = (pricePerUnit * quantity);
    } else {
        totalPrice = 0;
    }

    totalPriceSpan.textContent = Math.round(totalPrice) + ' €';
}