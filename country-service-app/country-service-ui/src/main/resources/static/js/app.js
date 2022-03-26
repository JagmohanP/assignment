'use strict'

const d = document;
d.addEventListener("DOMContentLoaded", function(event) {

	$("#countryName").change(function() {
		document.getElementById("detailsButton").focus();
	});

});

function getCountryDetails() {

	var countryName = document.getElementById("countryName").value;

	if (countryName == '' || countryName == '-1') {
		showErrorMessage("Please select a country");
		return;
	}

	disbaleDetailsButton();

	fetch('countries/'+countryName, {
		method: 'GET',
		headers: {
			'Content-Type': 'application/json',
		},
	})
		.then(response => {
			if (response.ok) {
				response.json().then(response => {
					if (response) {
						showResultMessage(response);
					}
					else {
						showErrorMessage(response.message);
					}
				});
			}
			else {
				response.json().then(response => {
					showErrorMessage(response.message);
				});
			}
			enableDetailsButton();
		})
		.catch((error) => {
			showErrorMessage('Error:' + error);
			enableDetailsButton();
		});
}

function showErrorMessage(messageToShow) {
	hideResultMessage();
	document.getElementById("error_msg_div").innerHTML = messageToShow;
	document.getElementById("error_msg_div").style.visibility = 'visible';
}

function hideErrorMessage() {
	document.getElementById("error_msg_div").style.visibility = 'hidden';
	document.getElementById("error_msg_div").innerHTML = "";
}

function showResultMessage(response) {
	hideErrorMessage();
	fillCountryDetailsData(response);
	document.getElementById("result_div").style.visibility = 'visible';
}

function hideResultMessage() {
	document.getElementById("result_div").style.visibility = 'hidden';
	clearCountryDetailsData();
}

function disbaleDetailsButton() {
	document.getElementById("detailsButton").disabled = true;
}
function enableDetailsButton() {
	document.getElementById("detailsButton").disabled = false;
}

function fillCountryDetailsData(response) {
	$("#div_country_name_title").html(response.name);
	$("#div_country_name").html(response.name);
	$("#div_country_code").html(response.country_code);
	$("#div_country_capital").html(response.capital.join(" , "));
	$("#div_country_population").html(response.population);
	$("#img_flag_file_url").attr("src", response.flag_file_url);
}

function clearCountryDetailsData() {
	$("#div_country_name_title").html("");
	$("#div_country_name").html("");
	$("#div_country_code").html("");
	$("#div_country_capital").html("");
	$("#div_country_population").html("");
	$("#img_flag_file_url").attr("src", "");
}
