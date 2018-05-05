$(document).ready(function() {
    
    $('select').select2();
    
    $(".js-example-basic-multiple-limit").select2({
        maximumSelectionLength: 2,
        ajax: {
            type: 'GET',
            q: 'dsdfv'
        }
        
    });
    
    $(".js-example-tokenizer").select2({
        tags: true,
        tokenSeparators: [',', ' '],
        placeholder: 'safcs'
    });
    
    var data = [{ id: 0, text: 'enhancement' }, { id: 1, text: 'bug' }, { id: 2, text: 'duplicate' }, { id: 3, text: 'invalid' }, { id: 4, text: 'wontfix' }];
 
    $(".js-example-data-array").select2({
        data: data
    });
    

    /*----Added------*/
    /*$('span.select2-container').on('click', function(event){
        event.preventDefault();
        var selecting = $('strong').filter('.select2-results__group').next().children();
        $(selecting).on('select', function(){
            console.log(this);
        });
    });*/
    /*-----END Added---*/
});