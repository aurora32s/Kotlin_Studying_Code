// JavaScript Document
 $(document).ready(function() {
  $('#part01').click(function() {//#part01??? ????? ?????
    $('#content').load('./ajax_part/abouthtml.html');//#content?? ??????? ?θ???.
    return false;
  });
});

 
 
 $(document).ready(function() {
   $('#part02').click(function() {
     $.getJSON('./js/aboutJSON.json' ,function(data) {
    $('#content').empty();
       $.each(data, function(entryIndex, entry) {
         var html = '<h2>' + entry['listN'] + '</h2>';
         html += '<h3>' + entry['sunmmary'] + '</h3>';
         html += '<div>' + entry['definition'] +'</div>' ;

         $('#content').append(html);
       });
     });
     return false;
   });
 });
 
  $(document).ready(function() {
   $('#part03').click(function() {
     $.get('./ajax_part/aboutXML.xml' ,function(data) {
    $('#content').empty();
     $(data).find('entry').each(function() {
        var $entry = $(this);
        var html = '<h2>' + $entry.attr('term') + '</h2>';
        html += '<h3>' + $entry.attr('part') + '</h3>';
        html += '<div>' + $entry.find('definition').text() +'</div>';
         $('#content').append(html);
       });
     });
     return false;
   });
 });
 
 $(document).ready(function() {
   $('#part02').click(function() {
     $.getJSON('./js/aboutJSON.json' ,function(data) {
    $('#content').empty();
       $.each(data, function(entryIndex, entry) {
         var html = '<h2>' + entry['listN'] + '</h2>';
         html += '<h3>' + entry['sunmmary'] + '</h3>';
         html += '<div>' + entry['definition'] +'</div>' ;
         $('#content').append(html);
       });
     });
     return false;
   });
 });
 

$(document).ready(function() {
  $('#part04').click(function() {
    $.getScript('./ajax_part/generaljvs.js');
    return false;
  });
});

