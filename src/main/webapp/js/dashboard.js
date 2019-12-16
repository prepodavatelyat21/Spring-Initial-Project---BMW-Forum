var currentUser;
var editVehicleId;
var currentUserVehicles = null;
function getCurrentUser() {
    $.ajax({
        url: '/currentUser',
        method: 'GET',
        async: false,
        success : function(response) {
            if (isNaN(response)) {
                window.location.href = "index.html";
            } else {
                currentUser = response;
            }
        },
        error : function() {
            window.location.href = "index.html";
        }
    });
}
function createTemplate(id, model, description, currentUserId, picture) {
    var template = $('#vehicle-template').clone();
    template.attr('id', id + '-vehicle');
    template.find('h5').text(model);
    template.find('p').html(description);
    template.find('img').attr('src', picture);
    var editButton = template.find('#edit-vehicle');
    var deleteButton = template.find('#delete-vehicle');
    if (currentUserId === currentUser) {
        editButton.attr('id', 'edit-vehicle-' + id);
        deleteButton.attr('id', 'delete-vehicle-' + id);
    } else {
        editButton.addClass('hidden');
        deleteButton.addClass('hidden');
    }
    template.removeClass('hidden');
    template.appendTo('#main-content');
}
function getAllVehicles() {
    $.ajax({
        url : "/vehicle/all",
        method : "GET",
        async: false,
        success : function(data) {
            data.forEach(function(obj) {
                createTemplate(obj.id, obj.model, obj.description, obj.user.id, obj.picture);
            });
        }
    });
}
function getAllCurrentUserVehicles() {
    $.ajax({
        url : "/vehicle/all/forCurrentUser",
        method : "GET",
        async: false,
        success : function(response) {
            if (response) {
                currentUserVehicles = response;
            }
        }
    });
}
$(function() {
    getCurrentUser();
    getAllVehicles();
    if (currentUser) {
        $('div.container').removeClass('hidden');
    }
    $('#logoutBtn').on('click', function(){
        $.ajax({
            url: '/logout',
            method: 'POST',
            success: function(response) {
                if (response.status === 200) {
                    window.location.href = "index.html";
                } else {
                    window.location.reload();
                }
            }
        });
    });
    $('#addVehicleForm').on('submit', function(e) {
        e.preventDefault();
        var images = [
            "/media/img/E21.jpg",
            "/media/img/E30.jpg",
            "/media/img/E36.jpg",
            "/media/img/E39.jpg",
            "/media/img/E46.jpg",
            "/media/img/E60.jpg",
            "/media/img/E63.jpg"
        ];
        var picture = images[Math.floor(Math.random()*images.length)];
        var model = $('#model').val();
        var description = $('#description').val();
        var price = $('#price').val();
        $.ajax({
            url: '/vehicle/add',
            method: 'POST',
            data: {
                model: model,
                description: description,
                price: price,
                picture: picture
            },
            success: function(response) {
                if (response === "error" || response === '-1') {
                    alert("Insertion of the vehicle failed, please try again!");
                } else {
                    createTemplate(response, model, description, currentUser, picture);
                    $('#model').val('');
                    $('#description').val('');
                    $('#price').val('');
                }
                $('#addVehicleModal').modal('hide');
            }
        })
    });
    $('.container').on('click', '.editVehicle', function (e) {
        editVehicleId = $(e.target).attr('id').split('-')[2];
        $.ajax({
            url: '/vehicle/edit',
            method: 'GET',
            data:{
                id: editVehicleId
            },
            success: function(response) {
                if (response) {
                    $('#editModel').val(response.model);
                    $('#editPrice').val(response.price);
                    $('#editDescription').val(response.description);
                    $('#editVehicleModal').modal();
                }
            },
            error: function() {
                alert('Something went wrong!');
            }
        });
    });
    $('#editVehicleForm').on('submit', function(e) {
        e.preventDefault();
        var model = $('#editModel').val();
        var description = $('#editDescription').val();
        var price = $('#editPrice').val();
        $.ajax({
            url: '/vehicle/edit',
            method: 'POST',
            data: {
                id: editVehicleId,
                model: model,
                description: description,
                price: price
            },
            success: function(response) {
                if (response) {
                    var updatedElem = $('#'+editVehicleId+'-vehicle');
                    updatedElem.find('h5').text(response.model);
                    updatedElem.find('p').html(response.description);
                }
                $('#editVehicleModal').modal('hide');
            },
            error: function() {
                alert("Insertion of the vehicle failed, please try again!");
            }
        })
    });
    $('.container').on('click', '.deleteVehicle', function (e) {
        var deletedId = $(e.target).attr('id').split('-')[2];
        $.ajax({
            url: '/vehicle/delete',
            method: 'DELETE',
            data:{
                id: deletedId
            },
            success: function(response) {
                if (response) {
                    $('#'+deletedId+'-vehicle').remove();
                }
            },
            error: function() {
                alert('Something went wrong! You are not able to delete it!');
            }
        });
    });
    $('#add-offer').on('click', function(e) {
        getAllCurrentUserVehicles();
        var select = $('#vehicle-options');
        select.find('option').remove();
        currentUserVehicles.forEach(function(obj){
            var option = new Option(obj.model, obj.id);
            $(option).html(obj.model);
            select.append(option);
        })
    });
    $('#addOfferForm').on('submit', function(e) {
        e.preventDefault();
        var city = $('#city');
        var vehicle = $('#vehicle-options');
        $.ajax({
            url: '/offer/add',
            method: 'POST',
            data: {
                city: city.val(),
                vehicleId: vehicle.val()
            },
            success: function(response) {
                if (response) {
                    alert('Offer saved!')
                } else {
                    alert('Something failed, try again')
                }
                $('#addOfferModal').modal('hide');
                city.val('');
                vehicle.val('');
                vehicle.find('option').remove();
            }
        })
    });
    $('#changeLayout').on('click', function(){
        var elements = $('#main-content').find('.template');
        var template = elements.first();
        if (template.hasClass('col-md-4')) {
            elements.removeClass('col-md-4');
            elements.addClass('col-md-12');
            $('.card-img-top').removeClass('fixed-height');
        } else {
            elements.removeClass('col-md-12');
            elements.addClass('col-md-4');
            $('.card-img-top').addClass('fixed-height');
        }
    });
});