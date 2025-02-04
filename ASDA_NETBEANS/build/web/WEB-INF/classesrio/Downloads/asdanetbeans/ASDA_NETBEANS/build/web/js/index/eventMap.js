/* author: Felipe Osorio Thomé */

/* Event mapping */
$(document).ready(function() {
    validateLogIn();

    $("#newUserLink").click(function() {
        openLightBox("shadowing", "wrapSignIn");
        /* Now, the container div is not 'display:none' anymore */
        validateSignIn();
    });

    $("#forgotPassLink").click(function() {
        openLightBox("shadowing", "wrapForgotPass");
        /* Now, the container div is not 'display:none' anymore */
        validateForgotPass();
    });

    $("#btSignInClose").click(function() {
        closeLightBox("shadowing", "wrapSignIn");
    });

    $("#btForgotPassClose").click(function() {
        closeLightBox("shadowing", "wrapForgotPass");
    });

    /* Centering the div. */
    $("#wrapLogin").css({
        top: $(window).height() > $("#wrapLogin").outerHeight() ?
            (($(window).height() - $("#wrapLogin").outerHeight()) / 2) * 0.8 : 0
    });

    $(window).resize(function() {
        $("#wrapLogin").css({
            top: $(window).height() > $("#wrapLogin").outerHeight() ?
                (($(window).height() - $("#wrapLogin").outerHeight()) / 2) * 0.8 : 0
        });
    });
});

$(document).bind('ajaxComplete', function(event, xhr, options) {
    var redirectHeader = xhr.getResponseHeader('fot-redirect');

    if (xhr.readyState == 4 && redirectHeader != null) {
        window.location.href = redirectHeader;
    }
});

function validateLogIn() {
    $('#login').validate({
        rules: {
            user: {
                required: true
            },
            password: {
                required: true
            }
        },
        messages: {
            user: {
                required: "Please enter your user"
            },
            password: {
                required: "Please enter your password."
            }
        },
        /* ---Ajax--- */
        submitHandler: function(form) {
            $(form).ajaxSubmit({
                url: "accounts?cmd=login",
                clearForm: true,
                error: function(xhr, ajaxOptions, thrownError) {
                    var errorHeader = xhr.getResponseHeader('fot-error');
                    
                    if(errorHeader != null) {
                        alert(errorHeader);
                    }else {
                        alert(thrownError);
                    }
                }
            });
        }
    });
}

function validateSignIn() {
    $('#signin').validate({
        rules: {
            si_email: {
                required: true,
                email: true
            },
            si_password: {
                required: true
            },
            si_password2: {
                required: true,
                equalTo: "#si_password"
            }
        },
        messages: {
            si_email: {
                required: "Please enter your email",
                email: "Please enter a valid email"
            },
            si_password: {
                required: "Please enter your password"
            },
            si_password2: {
                required: "Please repeat your password",
                equalTo: "Please enter the password as above"
            }
        },
        /* ---Ajax--- */
        submitHandler: function(form) {
            $(form).ajaxSubmit({
                url: "accounts?cmd=signin",
                clearForm: true,
                success: function() {
                    alert("Registration done successfully!");
                },
                error: function(xhr, ajaxOptions, thrownError) {
                    var errorHeader = xhr.getResponseHeader('fot-error');
                    
                    if(errorHeader != null) {
                        alert(errorHeader);
                    }else {
                        alert(thrownError);
                    }
                }
            });
        }
    });
}

function validateForgotPass() {
    $('#forgotPass').validate({
        rules: {
            fp_email: {
                required: true,
                email: true
            }
        },
        messages: {
            fp_email: {
                required: "Please enter your email",
                email: "Please enter a valid email"
            }
        },
        /* ---Ajax--- */
        submitHandler: function(form) {
            $(form).ajaxSubmit({
                url: "accounts?cmd=forgot-pass",
                clearForm: true
            });
        }
    });
}