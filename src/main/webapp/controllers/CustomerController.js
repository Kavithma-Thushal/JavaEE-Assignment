let baseUrl = "http://localhost:8080/javaee_pos/";

loadAllCustomer();

$("#btnSaveCustomer").click(function () {
    let formData = $("#customerForm").serialize();
    $.ajax({
        url: baseUrl + "customer",
        method: "POST",
        data: formData,
        dataType: "json",
        success: function (res) {
            loadAllCustomer();
            clearTextFields();
            alert("Customer Saved Successfully...!");
        }, error: function (error) {
            clearTextFields();
            console.log("Customer Saved Error...!");
        }
    });
});

$("#btnSearchCustomer").on("click", function () {
    searchCustomerId();
});

$("#searchCusId").on("keypress", function (event) {
    if (event.which === 13) {
        searchCustomerId();
    }
});

$("#btnUpdateCustomer").click(function () {
    let cusId = $("#txtCusId").val();
    let cusName = $("#txtCusName").val();
    let cusAddress = $("#txtCusAddress").val();
    let cusSalary = $("#txtCustomerSalary").val();

    const customerOb = {
        id: cusId,
        name: cusName,
        address: cusAddress,
        salary: cusSalary
    };

    $.ajax({
        url: baseUrl + "customer",
        method: "PUT",
        contentType: "application/json",
        data: JSON.stringify(customerOb),
        success: function (res) {
            loadAllCustomer();
            clearTextFields();
            alert("Customer Updated Successfully...!");
        },
        error: function (error) {
            clearTextFields();
            alert("Customer Updated Error...!");
        }
    });
});

$("#btnDeleteCustomer").click(function () {
    let cusId = $("#txtCusId").val();
    let cusName = $("#txtCusName").val();
    let cusAddress = $("#txtCusAddress").val();
    let cusSalary = $("#txtCustomerSalary").val();

    const customerOb = {
        id: cusId,
        name: cusName,
        address: cusAddress,
        salary: cusSalary
    };

    $.ajax({
        url: baseUrl + "customer",
        method: "DELETE",
        contentType: "application/json",
        data: JSON.stringify(customerOb),
        success: function (res) {
            loadAllCustomer();
            clearTextFields();
            alert("Customer Deleted Successfully...!");
        },
        error: function (error) {
            clearTextFields();
            alert("Customer Deleted Error...!");
        }
    });
});

$('#btnGetAllCustomers').click(function () {
    loadAllCustomer();
});

/**
 * Search Customer
 **/
function searchCustomerId() {
    var search = $("#searchCusId").val();
    $("#customerTable").empty();
    $.ajax({
        url: baseUrl + "customer?id=" + search + "&option=searchCusId",
        method: "GET",
        contentType: "application/json",
        dataType: "json",
        success: function (res) {
            let row = "<tr><td>" + res.id + "</td><td>" + res.name + "</td><td>" + res.address + "</td><td>" + res.salary + "</td></tr>";
            $("#customerTable").append(row);
            blindClickEvents();
            $('#searchCusId').val('');
        },
        error: function (error) {
            loadAllCustomer();
            $('#searchCusId').val('');
            alert("Invalid Customer ID");
        }
    });
}

/**
 * Load All Customers
 **/
function loadAllCustomer() {
    $("#customerTable").empty();
    $.ajax({
        url: baseUrl + "customer?option=loadAllCustomer",
        method: "GET",
        dataType: "json",
        success: function (res) {
            for (let i of res.data) {
                let id = i.id;
                let name = i.name;
                let address = i.address;
                let salary = i.salary;

                let row = "<tr><td>" + id + "</td><td>" + name + "</td><td>" + address + "</td><td>" + salary + "</td></tr>";
                $("#customerTable").append(row);
            }
            blindClickEvents();
        },
        error: function (error) {
            console.log("Load All Customers Error...!");
        }
    });
}

/**
 * Clear Text Fields
 **/
function clearTextFields() {
    $('#txtCusId').val('');
    $('#txtCusName').val('');
    $('#txtCusAddress').val('');
    $('#txtCustomerSalary').val('');
}

/**
 * Table Listener (Click and Load Text Fields)
 **/
function blindClickEvents() {
    $("#customerTable>tr").on("click", function () {
        let id = $(this).children().eq(0).text();
        let name = $(this).children().eq(1).text();
        let address = $(this).children().eq(2).text();
        let salary = $(this).children().eq(3).text();

        $("#txtCusId").val(id);
        $("#txtCusName").val(name);
        $("#txtCusAddress").val(address);
        $("#txtCustomerSalary").val(salary);
    });
}