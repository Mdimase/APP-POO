/**
 * Función que despliega/cierra una alerta de problema/éxito luego de realizar una acción.
 */
/*
$(document).ready(function(){
	$("#alert").show(500, function(){
		$("#alert").delay(5000).hide(500);
	});
});
*/

/*
$(document).ready(function(){
	$("#alert").fadeTo(500, 1, function(){
		$("#alert").delay(5000).fadeOut(500);
	});
});
*/


$(document).ready(function(){
	$("#alert").slideToggle(500, function(){
		$("#alert").delay(5000).slideToggle(500);
	});
});

