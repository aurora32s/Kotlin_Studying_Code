//var farm_url='';
var farm_url='http://e-ati.jeju.go.kr';

//http://192.168.0.50:8080/
//http://farm.infomind.co.kr:8080/
//var farm_url='http://100.100.100.1:8080';
//var farm_url='http://192.168.100.2:8080';

//파일다운로드 관련 url
var file_down = "http://www.agri.jeju.kr/common/board/result/download.wiz?pkCode=";

//모바일 디바이스 체크 후 뷰포트 스케일 설정
$(function(){
	//check browser
	var isie=(/msie/i).test(navigator.userAgent); //ie
	var isie6=(/msie 6/i).test(navigator.userAgent); //ie 6
	var isie7=(/msie 7/i).test(navigator.userAgent); //ie 7
	var isie8=(/msie 8/i).test(navigator.userAgent); //ie 8
	var isie9=(/msie 9/i).test(navigator.userAgent); //ie 9
	var isfirefox=(/firefox/i).test(navigator.userAgent); //firefox
	var isapple=(/applewebkit/i).test(navigator.userAgent); //safari,chrome
	var isopera=(/opera/i).test(navigator.userAgent); //opera
	var isios=(/(ipod|iphone|ipad)/i).test(navigator.userAgent);//ios
	var isipad=(/(ipad)/i).test(navigator.userAgent);//ipad
	var isandroid=(/android/i).test(navigator.userAgent);//android
	if(isie7 || isie8 || isie9) isie6=false;
	if(isie9) isie=false;
	
	//아이패드인 경우
	if(isipad == true){
		$("meta[name=viewport]").attr("content","user-scalable=yes, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=640");
	}	
});

/*
	함수명	: 이미지명 치환
	작성자	: 장호연
	작성일	: 2011.10.13
	코멘트	- 넘겨받은 오브젝트의 src를 변경한다.
			- obj 	= 대상 img
			- flag 	= on, off
*/
function imgReplace(obj,flag){
	var imgSrc = obj.attr("src");
	if(flag == "on"){
		imgSrc = imgSrc.replace("_off.","_on.");
	} else if(flag == "off"){
		imgSrc = imgSrc.replace("_on.","_off.");
	}
	obj.attr("src",imgSrc);
}
//sns링크
function go_sns(){ 
	url = "http://e-ati-1.jeju.go.kr";
	//url = "http://sns.infomind.co.kr:8080"; 
	//window.ASNS.setLoadUrl(url);
	
	if(window.ASNS){
		if(parseFloat(window.ASNS.getPhoneVer()) >= 4.4){
			location.href = url ;
		}
		else {
			window.ASNS.setLoadUrl(url);	
		}	
		
	 }else{
		 location.href = url ;
	 }
	
}


function go_50(){
alert();
	url = "http://ipm.agri.jeju.kr/develope/weather/weather0301_m.php";
	//url = "http://sns.infomind.co.kr:8080";
	//window.ASNS.setLoadUrl(url);

	if(window.ASNS){
		if(parseFloat(window.ASNS.getPhoneVer()) >= 4.4){
			location.href = url ;
		}
		else {
			window.ASNS.setLoadUrl(url);
		}

	 }else{
		 location.href = url ;
	 }

}

//pdf 보기 
function go_pdf(url){
	 
	
	if(window.ASNS){
		 window.ASNS.viewPDF(url)  ;
	 }else{
		 location.href = url ;
	 }
}


//pdf 보기
function go_pdf2(url){

    // alert(url);
    if(window.ASNS){
        window.ASNS.viewPDF(url)  ;
    }else{
        location.href = url ;
    }
}




//사이트맵 열고 닫기
function sitemap_onoff(){
	var flag = "closed";
	if($('.sitemap04').size() != 0){
		$('.sitemap > a').click(function(){
			if(flag == "closed"){
				$('.sitemap04').show();
				imgReplace($(this).children(),"on");
				flag = "open";
			} else if(flag == "open"){
				$('.sitemap04').hide();
				imgReplace($(this).children(),"off");
				flag = "closed";
			}
			return false;
		});
	}

	if($('.sitemap06').size() != 0){
		var flag = "closed";
		$('.sitemap > a').click(function(){
			if(flag == "closed"){
				$('.sitemap06').show();
				imgReplace($(this).children(),"on");
				flag = "open";
			} else if(flag == "open"){
				$('.sitemap06').hide();
				imgReplace($(this).children(),"off");
				flag = "closed";
			}
			return false;
		});
	}
}  

function localSave(url, param, target){
	//window.localStorage.setItem("jsonObject", param);
	for (var key in param) {
		value = param[key];
		window.localStorage.setItem(key, value);
   } 
   location.href = url ;
}

//맨위로 버튼
function back_top()
{
       x = document.body.scrollLeft;
       y = document.body.scrollTop;
       step = 2;

       while ((x != 0) || (y != 0)) {
               scroll (x, y);
               step += (step * step / 300);
               x -= step;
               y -= step;
               if (x < 0) x = 0;
               if (y < 0) y = 0;
       } 
       scroll (0, 0);
}

//로딩중
function imagefade(){
	 $("#info").fadeOut(800).fadeIn(800).fadeOut(400).fadeOut(400).fadeIn(400,imagefade);
}