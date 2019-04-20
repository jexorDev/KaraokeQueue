$(window).on('load',function(){
	$('#kioskLoginModal').modal({backdrop: 'static', keyboard: false, show: true});
});

$(document).ready(function() {
	$("#credential-continue").click(function(){
		console.log($('#credential-username').val());
		console.log($('#credential-password').val());
		
		var credentials = {}
		credentials["username"] = $('#credential-username').val();
		credentials["password"] = $('#credential-password').val();
		
		 $.ajax({
		        type: "POST",
		        contentType: "application/json",
		        url: "/user/get/id",
		        data: JSON.stringify(credentials),
		        dataType: 'json',
		        cache: false,
		        timeout: 600000,
		        success: function (data) {
		            
		            if (data["msg"] == "success") {
		            	$('#kiosk-user-id').val(data["result"]);	
		            	$('#kioskLoginModal').modal('hide');
		            }
		            else
	            	{
		            	$('#credential-invalid').removeAttr("hidden");
	            	}            
		            

		        },
		        error: function (e) {

		            console.log("ERROR : ", e);

		        }
		    });
	});	
});

 
