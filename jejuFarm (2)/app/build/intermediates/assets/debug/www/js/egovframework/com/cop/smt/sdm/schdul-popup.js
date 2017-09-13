

function fn_orgnzt_list(url) {
			
	$.getJSON(url, $('#orgnzt_searchVO').serialize(), function(json){

		$("#searchCondition option[value='" + json.searchVO.searchCondition + "']").attr('selected', 'selected');	
		var html = "";
            
		if(json.resultList.length == 0) {
			html += '<li class="com-egovNodata">';
       		html += '    <spring:message code="common.nodata.msg"/>';
       		html += '</li>';
		}
		else {
			for ( var i = 0; i < json.resultList.length; i++) {
				
				var result =  json.resultList[i];
				
				html += '<li>';
				html += '	<a href="javascript:fn_sel_orgn(\'' + result.orgnztId + '\', \'' + result.orgnztNm + '\')">';
				html += '		<h3>' + result.orgnztNm + '</h3>';
				html += '		<p>' + result.orgnztDc + '</p>';
				html += '	</a>';
				html += '</li>';			
							
			}
		}

		$('div[id="view_orgnzt"] ul[data-role="listview"]').html(html).listview('refresh');
		
 	});
 	
}

function fn_emplyr_list(url) {
	
		$.getJSON(url, $('#emplyr_searchVO').serialize(), function(json){

		$("#searchCondition option[value='" + json.searchVO.searchCondition + "']").attr('selected', 'selected');	
		var html = "";
            
		if(json.resultList.length == 0) {
			html += '<li class="com-egovNodata">';
       		html += '    <spring:message code="common.nodata.msg"/>';
       		html += '</li>';
		}
		else {
			for ( var i = 0; i < json.resultList.length; i++) {
				
				var result =  json.resultList[i];
				
				html += '<li>';
				html += '	<a href="javascript:fn_sel_emp(\'' + result.esntlId + '\', \'' + result.userNm + '\')">';
				html += '		<h3>' + result.userNm + '</h3>';
				html += '		<p>' + result.emplyrId + '</p>';
				html += '		<p>' + result.offmTelno + '</p>';
				html += '	</a>';
				html += '</li>';			
							
			}
		}
			$('div[id="view_emplyr"] ul[data-role="listview"]').html(html).listview('refresh');
		
	 	});
	 	
}
