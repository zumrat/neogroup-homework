const list = document.getElementById("list");
const countries = document.getElementById("countries");
const form = document.getElementById("phone_number_input_form");
const phone_number_input = document.getElementById("phone_number");
const phone_number_validation_error = document.getElementById("phone_number_validation_error");

form.addEventListener("submit", onFormSubmit);
form.addEventListener("reset", onFormReset);

function onFormReset (event) {
  phone_number_input.style.color = 'black';
  phone_number_validation_error.style.display = 'none';

  list.textContent = '';
  countries.style.display = 'none';
}

async function onFormSubmit (event) {
  try {
    event.preventDefault();

    phone_number_input.style.color = 'black';
    phone_number_validation_error.style.display = 'none';

    const phone_number = phone_number_input.value;

    const formData = new FormData();
    formData.append("number", phone_number);

    const response = await fetch(`http://localhost:8088/api/v1/phoneValidator/countries?number=${phone_number}`, {
      method: "GET"
    });
    const data = await response.json()

    if (data?.length) {
      countries.style.display = 'flex';
      list.textContent = data.map((country, index) => `${index + 1}. ${country.name}, (${country.codes.map(item => item.countryCode).join(', ')})`).join('\r\n');
    } else {
      countries.style.display = 'none';
      phone_number_input.style.color = 'red';
      phone_number_validation_error.style.display = 'flex';
      phone_number_validation_error.textContent = "Was not able to find country by phone number"
    }
  } catch (err) {
    countries.style.display = 'none';
    phone_number_input.style.color = 'red';
    phone_number_validation_error.style.display = 'flex';

    phone_number_validation_error.textContent = err
  }
}
