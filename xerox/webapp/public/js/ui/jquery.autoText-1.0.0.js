(function($) {
	$.fn.autoText = function(options) {
		var opts = $.extend({}, $.fn.autoText.defaults, options); /* build main options before element iteration */
		return this.each(function() { /* iterate and reformat each matched element */
			var io = opts;
			$(this).keydown(function(e){ /* start keyDown event */
				
			}).keypress(function(e){
	  			var key = e.which,
					keye = e.keyCode,
					tecla = String.fromCharCode(key).toLowerCase(),
					letras = io.caracteres;
			    if(letras.indexOf(tecla)==-1 && keye!=9&& (key==37 || keye!=37)&& (keye!=39 || key==39) && keye!=8 && (keye!=46 || key==46) || key==161){
			    	e.preventDefault();
			    }
			}).keyup(function(e){
				
			}).bind('change focusout', function(){
				
			}).bind('paste', function(){
				// setTimeout(function(){autoCheck(iv, ii, io);}, 0); 
			});
		});
	};
	$.fn.autoText.defaults = {
		caracteres: " 0123456789abcdefghijklmnopqrstuvwxyz\u00ED\u00FA\u00F3\u00E9\u00E1\u00F1"
	}; 
	
})(jQuery);