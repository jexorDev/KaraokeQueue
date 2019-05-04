$(window).on('load',function(){
	$('#kioskLoginModal').modal({backdrop: 'static', keyboard: false, show: true});
});

$(document).ready(function() {

	$("#credential-continue").click(function(){
		
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
		            	$('#kiosk-user-id').val(data["userId"]);	
		            	$('#inquiry-header').text("Showing requests for " + data['userName']);
		            	$('#kiosk-user-id').trigger('change');
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
	
	
	$("#kiosk-user-id").change(function() {
		 if ($("#song-requests-list").length) {
					 
					 $.ajax({
					        type: "GET",
					        contentType: "html",
					        url: "/request/view/" + $("#kiosk-user-id").val(),      
					        data: {},
					        cache: false,
					        timeout: 600000,
					        success: function (data) {
					            
					        	$("#song-requests-list").replaceWith(data);
					        			
					        },
					        error: function (e) {
			
					            console.log("ERROR : ", e);
			
					        }
					    });
				 }
		 
		 if ($("#vote-list").length) {
			 
			 $.ajax({
			        type: "GET",
			        contentType: "html",
			        url: "/vote/" + $("#kiosk-user-id").val(),      
			        data: {},
			        cache: false,
			        timeout: 600000,
			        success: function (data) {
			            
			        	$("#vote-list").replaceWith(data);
			        			
			        },
			        error: function (e) {
	
			            console.log("ERROR : ", e);
	
			        }
			    });
		 }
	});
});

 
