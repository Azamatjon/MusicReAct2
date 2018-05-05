$(document).ready(function() {
    
    

    $('.input-daterange').datepicker({
    	format: "yyyy-mm-dd",
    	weekStart: 1,
    	autoclose: true,
    	todayHighlight: true
	});

    // ???
	$('.summernote').summernote({
	  height: 350
	});
    $('#cp1').colorpicker({
        format: 'hex'
    });
    $('#cp2').colorpicker();
});