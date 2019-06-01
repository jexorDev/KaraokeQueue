$(document).delegate('.queue-form', 'submit', function(e) {

	e.preventDefault();
	$form = $(this); //wrap this in jQuery


    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: $form.attr('action'), //admin/queue/{id}
        data: $form.serializeArray()[0].value,	//sequence        
        cache: false,
        timeout: 600000,
        success: function (data) {
        	reloadRequests();
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });    
	
});

$(document).delegate('.complete-form', 'submit', function(e) {

	e.preventDefault();
	$form = $(this); //wrap this in jQuery
	
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: $form.attr('action'), //admin/complete/{id}       
        cache: false,
        timeout: 600000,
        success: function (data) {
        	reloadRequests();
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });    
	
});

$(document).delegate('.cancel-form', 'submit', function(e) {

	e.preventDefault();
	$form = $(this); //wrap this in jQuery
	
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: $form.attr('action'), //admin/complete/{id}       
        cache: false,
        timeout: 600000,
        success: function (data) {
        	reloadRequests();
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });    
	
});

function reloadRequests() {
	 
	$.ajax({
	        type: "GET",
	        contentType: "html",
	        url: "/admin/requests/",      
	        data: {},
	        cache: false,
	        timeout: 600000,
	        success: function (data) {
	            
	        	$("#current-requests").replaceWith(data);
	        			
	        },
	        error: function (e) {
	            console.log("ERROR : ", e);
	        }
	    });
	
	$.ajax({
        type: "GET",
        contentType: "html",
        url: "/admin/statistics/",      
        data: {},
        cache: false,
        timeout: 600000,
        success: function (data) {
            
        	$("#user-statistics").replaceWith(data);
        			
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
	  });
}


	
