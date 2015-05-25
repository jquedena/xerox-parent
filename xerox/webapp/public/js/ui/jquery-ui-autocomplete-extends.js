(function($) {
	$.widget("ui.combobox", {
		_create : function() {
			var input = "",
				button = "",
				that = this,
				wasOpen = false,
				select = this.element.hide(),
				selected = select.children(":selected"),
				value = selected.val() ? selected.text() : "",
				wrapper = this.wrapper = $("<table cellpadding=0 cellspacing=0>").addClass("ui-combobox").insertAfter(select),
				wrapper_tr = $("<tr>").appendTo(wrapper),
				wrapper_td_input = $("<td>").appendTo(wrapper_tr).css({"verticalAlign": "top"}),
				wrapper_td_button = $("<td>").appendTo(wrapper_tr).css({"verticalAlign": "top"});
				
			function removeIfInvalid(element) {
				var value = $(element).val(),
					matcher = new RegExp("^" + $.ui.autocomplete.escapeRegex(value) + "$", "i"),
					valid = false;
				
				select.children("option").each(function() {
					if ($(this).text().match(matcher)) {
						this.selected = valid = true;
						return false;
					}
				});
				
				if (!valid) {
					// remove invalid value, as it didn't match
					// anything
					$(element).val("").attr("title", "No existen coincidencias para el valor " + value).tooltip("open");
					select.val("");
					setTimeout(function() {
						input.tooltip("close").attr("title", "");
					}, 2500);
					input.data("ui-autocomplete").term = "";
				}
			}
			
			input = $("<input>")
				.appendTo(wrapper_td_input)
				.val(value)
				.css({"width": select.css("width")})
				.attr("title", "")
				.attr("id", "txt_" + select.attr("id"))
				.addClass("ui-combobox-input")
				.autocomplete({
					delay : 0,
					minLength : 0,
					source : function(request, response) {
						var matcher = new RegExp($.ui.autocomplete.escapeRegex(request.term), "i");
						response(select
							.children("option")
							.map(function() {
									var text = $(this).text();
									if (this.value && (!request.term || matcher.test(text)))
										return {
											label : text.replace(new RegExp("(?![^&;]+;)(?!<[^<>]*)(" + $.ui.autocomplete.escapeRegex(request.term) + ")(?![^<>]*>)(?![^&;]+;)", "gi"), "<strong>$1</strong>"),
											value : text,
											option : this
										};
							})
						);
					},
					select : function(event, ui) {
						var inputText = "#" + $(this).attr('id'),
							inputHidden = inputText.replace("txt_", "hdn_");
						
						if(ui.item) {
							$(inputHidden).val(ui.item.option.value);
						}
						
						ui.item.option.selected = true;
						that._trigger("selected", event, {item : ui.item.option});
					},
					change : function(event, ui) {
						if (!ui.item) {
							removeIfInvalid(this);
						}
					}
				})
				.addClass("ui-widget ui-widget-content ui-corner-left");
			
			input.data("ui-autocomplete")._renderItem = function(ul, item) {
				return $("<li>")
					.append("<a>" + item.label + "</a>")
					.appendTo(ul);
			};
				
			button = $("<button>").attr("tabIndex", -1).appendTo(wrapper_td_button)
				.attr("id", "btn_" + select.attr("id"))
				.button({
					icons : {primary : "ui-icon-triangle-1-s"},
					text : false
				})
				.removeClass("ui-corner-all").addClass("ui-corner-right ui-combobox-toggle")
				.mousedown(function() {
					wasOpen = input.autocomplete("widget").is(":visible");
				})
				.click(function() {
					input.focus();
					// close if already visible
					if (wasOpen) {
						return;
					}
					// pass empty string as value to search
					// for, displaying all results
					input.autocomplete("search", "");
				});
			
			$("<input type=\"hidden\">").insertAfter(button)
				.attr("id", "hdn_" + select.attr("id"))
				.val("-1");
			
			input.tooltip({
				tooltipClass : "ui-state-highlight"
			});
		},
		
		_destroy : function() {
			this.wrapper.remove();
			this.element.show();
		}
	});
})(jQuery);