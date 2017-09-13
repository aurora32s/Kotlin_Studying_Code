function fn_egov_open_Popup(url) {
					
					$.getJSON(url, $('#searchPopVO').serialize(), function(json){

						var html = "";
				            
						if(json.resultList.length == 0) {
							html += '<li class="com-egovNodata">';
				       		html += '    <spring:message code="common.nodata.msg"/>';
				       		html += '</li>';
						}
						else {
							for ( var i = 0; i < json.resultList.length; i++) {
								
								var result =  json.resultList[i];
								var kndCode = result.schdulKndCode == "1" ? "부서일정" : "개인일정";
								
								html += '<li>';
								html += '   <a href="javaScript:fn_sel_schdul(\'' + result.schdulId + '\', \'' + result.schdulNm + '\')" data-ajax="false">';	
								html += '		<h3>' + result.schdulNm + '</h3>';
								html += '		<p>' + result.schdulCn + '</p>';
								html += '	</a>';
								html += '</li>';			
											
							}
						}

						$('div[id="view_popup"] ul[data-role="listview"]').html(html).listview('refresh');
						
				 	});
				 	
				}

				function fn_sel_schdul(schdulId, schdulNm) {
					
					$('#schdulId').val(schdulId);
					$('#schdulCn').val(schdulNm);

					$.mobile.changePage($("#view"));
				}
