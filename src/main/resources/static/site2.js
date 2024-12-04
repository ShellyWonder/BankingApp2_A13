function calcPayment(amount, rate, term) {
    return (amount * rate) / (1 - Math.pow(1 + rate, -term));
}

function calcInterest(balance, rate) {
    return balance * rate;
}

function calcRate(rate) {
    return rate = rate / 1200;
}

function getValues() {
    let loanAmount = document.getElementById("loanAmount").value;
    let loanTerm = document.getElementById("loanTerm").value;
    let loanRate = document.getElementById("loanRate").value;


    loanAmount = Number(loanAmount);
    loanTerm = parseInt(loanTerm);

    loanRate = parseFloat(loanRate);
    loanRate = calcRate(loanRate);

    //calculate a payment
    let payment = calcPayment(loanAmount, loanRate, loanTerm);

    //return a list of payment objects
    let payments = getPayments(loanAmount, loanRate, loanTerm, payment);

    displayData(payments, loanAmount, payment);

}

function getPayments(amount, rate, term, payment) {
    let payments = [];

    let balance = amount;
    let totalInterest = 0;
    let monthlyPrincipal = 0;
    let monthlyInterest = 0;

    for (let month = 1; month <= term; month++) {
        monthlyInterest = calcInterest(balance, rate);
        totalInterest += monthlyInterest;
        monthlyPrincipal = payment - monthlyInterest;
        balance = Math.abs(balance - monthlyPrincipal);

        //add the details to an object
        let curPayment = {
            month: month,
            payment: payment,
            principal: monthlyPrincipal,
            interest: monthlyInterest,
            totalInterest: totalInterest,
            balance: balance
        }
        payments.push(curPayment);
    }
    return payments;
}

function displayData(payments, amount, payment) {
    let tableBody = document.getElementById("scheduleBody");
    let template = document.getElementById("schedule-Template");

    //clears the table of any previous data
    tableBody.innerHTML = "";

    //configure currency formatter
    let currencyFormatter = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    });

    for (let i = 0; i < payments.length; i++) {
        let payRow = template.content.cloneNode(true);
        let payCols = payRow.querySelectorAll("td");
        
        payCols[0].textContent = payments[i].month;
        payCols[1].textContent = currencyFormatter.format(payments[i].payment.toFixed(2));
        payCols[2].textContent = currencyFormatter.format(payments[i].principal.toFixed(2));
        payCols[3].textContent = currencyFormatter.format(payments[i].interest.toFixed(2));
        payCols[4].textContent = currencyFormatter.format(payments[i].totalInterest.toFixed(2));
        payCols[5].textContent = currencyFormatter.format(payments[i].balance.toFixed(2));
    
        tableBody.appendChild(payRow)
    }

    //total interest is in the last row of the payments array.
    let totalInterest = payments[payments.length-1].totalInterest;

    //calculate the total cost
    let totalCost = Number(amount) + totalInterest;

    let totalPrincipal = Number(amount)

    let labelPrincipal = document.getElementById("totalPrincipal");
    labelPrincipal.innerHTML = totalPrincipal.toLocaleString("en-US",{
        style: "currency",
        currency: "USD"
    });

    let labelInterest = document.getElementById("totalInterest");
    labelInterest.innerHTML = Number(totalInterest).toLocaleString("en-US",{
        style: "currency",
        currency: "USD"
    });

    let labelPayment = document.getElementById("payment");
    labelPayment.innerHTML = Number(payment).toLocaleString("en-US", {
        style: "currency",
        currency: "USD"
    });

    let labelTotalCost = document.getElementById("totalCost");
    labelTotalCost.innerHTML = Number(totalCost).toLocaleString("en-US",{
        style: "currency",
        currency: "USD"
    });

    showPrintButton();
}

function showPrintButton() {
    let printSection = document.getElementById('printSection');
    if (document.getElementById('scheduleBody').hasChildNodes()) {
        printSection.classList.remove('d-none');
    } else {
        printSection.classList.add('d-none');
    }
}
function updateCopyrightYear() {
    const currentYear = new Date().getFullYear();
    document.getElementById("copyrightYear").textContent = currentYear;
}

document.addEventListener('DOMContentLoaded', updateCopyrightYear);
