$('#username, #email').on('change', function(e) {
    var element = $(e.target);
    var $errorDiv = element.closest('div').find('.validation-error');
    var ajaxUrl = '';
    var data = {};
    if (element.attr('id') === 'username') {
        ajaxUrl = '/validate/username';
        data['username'] = element.val();
    } else {
        ajaxUrl = '/validate/email';
        data['email'] = element.val();
    }
    if (element.val()) {
        $.ajax({
            url: ajaxUrl,
            method: 'GET',
            data: data,
            success:function(response){
                if (response) {
                    $errorDiv.text('That ' + element.attr('name') + ' is already in use!');
                    $errorDiv.removeClass('hidden');
                } else {
                    $errorDiv.addClass('hidden');
                }
            }
        });
    } else {
        $errorDiv.text('That field is required!');
        $errorDiv.removeClass('hidden');
    }
});
$('#confirmPassword').on('change', function(e) {
    $this = $(e.target);
    var $errorDiv = $this.closest('div').find('.validation-error');
    $password = $('#password');
    if ($this.val()) {
        if ($password.val() && $this.val() !== $password.val()) {
            $errorDiv.text('The passwords do not match!');
            $errorDiv.removeClass('hidden');
        } else {
            $errorDiv.text('');
            $errorDiv.addClass('hidden');
        }
    } else {
        $errorDiv.text('That field is required!');
        $errorDiv.removeClass('hidden');
    }
});
$('#password').on('change', function(e) {
    $this = $(e.target);
    var $errorDiv = $this.closest('div').find('.validation-error');
    $confirmPass = $('#confirmPassword');
    if ($this.val()) {
        if ($confirmPass.val() && $this.val() !== $confirmPass.val()) {
            $errorDiv.text('The passwords do not match!');
            $errorDiv.removeClass('hidden');
        } else {
            $errorDiv.text('');
            $errorDiv.addClass('hidden');
        }
    } else {
        $errorDiv.text('That field is required!');
        $errorDiv.removeClass('hidden');
    }
});
$('#loginForm').submit(function(e){
    e.preventDefault();
    var errElem = $('.login-error');
    $.ajax({
        url:"/login",
        method: "POST",
        data:{
            username:$("#usernameLogin").val(),
            password:$("#passwordLogin").val()
        },
        success:function(data){
            if (data === "") {
                errElem.removeClass('alert-success');
                errElem.addClass('alert-danger');
                errElem.text('Username or password is incorrect.');
                errElem.removeClass('hidden');
                $("#usernameLogin").val('');
                $("#passwordLogin").val('')
            } else {
                window.location.replace(data);
            }
        },
        error:function(){
            errElem.removeClass('alert-success');
            errElem.addClass('alert-danger');
            errElem.text('Username or password is incorrect.');
            errElem.removeClass('hidden');
            $("#usernameLogin").val('');
            $("#passwordLogin").val('')
        }
    });
});
$('#registerForm').submit(function(e){
    e.preventDefault();
    var errElem = $('.login-error');
    var username = $('#username');
    var email = $('#email');
    var password = $('#password');
    var confirmPassword = $('#confirmPassword');
    $.ajax({
        url: '/register',
        method: 'POST',
        data: {
            username: username.val(),
            email: email.val(),
            password: password.val(),
            confirmPassword: confirmPassword.val()
        },
        success: function(response) {
            if (response) {
                errElem.removeClass('alert-danger');
                errElem.addClass('alert-success');
                errElem.text('Registration successful. Now you can sign in');
                errElem.removeClass('hidden');
            } else {
                errElem.removeClass('alert-success');
                errElem.addClass('alert-danger');
                errElem.text('Something went wrong! Try again!');
                errElem.removeClass('hidden');
            }
            $('#registerModal').modal('hide');
            username.val('');
            email.val('');
            password.val('');
            confirmPassword.val('');
            $('.validation-error').addClass('hidden');
        }
    });
});