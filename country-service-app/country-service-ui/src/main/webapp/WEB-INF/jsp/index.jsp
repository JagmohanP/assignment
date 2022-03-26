<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Country details</title>

<!-- Bootstrap core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">


</head>
<body class="bg-light">

	<div class="container">

		<div class="row">
			<div class="d-flex justify-content-center p-2">
				<div class="card p-2">
					<div class="input-group">
						<select class="form-select" id="countryName" required>
							<option value="-1" selected="selected" disabled="disabled">
							------------------- Select country -------------------</option>
							<c:forEach var="currency" items="${countries}">
								<option value="${currency.key}">${currency.key}
									(${currency.value})</option>
							</c:forEach>
						</select>
						<div class="input-group-append px-2">
							<button class="btn btn-primary btn-sm px-2" type="button"
								id="detailsButton" onclick="getCountryDetails();">Get
								details</button>
						</div>
					</div>
				</div>
			</div>

			<div class="row d-flex aligns-items-center justify-content-center">
				<div class="col-md-9">
					<hr class="my-4">
				</div>
			</div>

			<div class="row d-flex aligns-items-center justify-content-center">
				<div class="col-md-9">
					<div class="alert alert-danger" role="alert" id="error_msg_div"
						style="visibility: hidden;"></div>
					<div id="result_div" style="visibility: hidden;">
						<div class="row">
							<div class="col-sm-4">
								<div class="card">
									<div class="card-body">
										<img class="card-img-top" id="img_flag_file_url" src="data:,"
											alt="Flag photo"
											style="height: 150px; width: 100%; display: block;">
									</div>
								</div>
							</div>
							<div class="col-sm-8">
								<div class="card">
								<h5 class="card-header text-center" id="div_country_name_title"></h5>
  
									<div class="card-body">
									<!-- <h5 class="card-title font-weight-bold"
											id="div_country_name_title"></h5> -->
										<div class="row">
											<div class="col-md-3 mb-1">
												<label style="font-weight:bold;">Country Name :</label>
											</div>
											<div class="col-md-6 mb-1">
												<div id="div_country_name"></div>
											</div>
										</div>
										<div class="row">
											<div class="col-md-3 mb-1">
												<label style="font-weight:bold;">Country Code :</label>
											</div>
											<div class="col-md-6 mb-1">
												<div id="div_country_code"></div>
											</div>
										</div>
										<div class="row">
											<div class="col-md-3 mb-1">
												<label style="font-weight:bold;">Capital :</label>
											</div>
											<div class="col-md-6 mb-1">
												<div id="div_country_capital"></div>
											</div>
										</div>
										<div class="row">
											<div class="col-md-3 mb-1">
												<label style="font-weight:bold;">Population :</label>
											</div>
											<div class="col-md-6 mb-1">
												<div id="div_country_population"></div>
											</div>
										</div>
									</div>


								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>

	<script src="js/jquery.slim.min.js"></script>
	<script src="js/bootstrap.bundle.min.js"></script>
	<script src="js/app.js"></script>
</body>
</html>
