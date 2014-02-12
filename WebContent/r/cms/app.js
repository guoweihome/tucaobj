bt_alert = function() {}
bt_alert.warning = function(id,message) {
	$('#'+id).hide().html('<div class="alert"><a class="close" data-dismiss="alert">×</a><span>'+message+'</span></div>').fadeIn('slow');
}
bt_alert.info = function(id,message) {
	$('#'+id).hide().html('<div class="alert alert-info"><a class="close" data-dismiss="alert">×</a><span>'+message+'</span></div>').fadeIn('slow');
}
bt_alert.error = function(id,message) {
	$('#'+id).hide().html('<div class="alert alert-error"><a class="close" data-dismiss="alert">×</a><span>'+message+'</span></div>').fadeIn('slow');
}
$("document").ready(function(){
	
	$('body').on('hidden', '.modal', function () {
		//alert("test");
		$(this).removeData('modal');
	});
   //二维码
   $("#qcImg").popover({"html":true,"content":$("#show_qc_content").html()}).click(function(e) {e.preventDefault()});
   $("#login_form input[type=text]").keydown(function(e){if(e.which ==13) {	$("#loginBtn").click();}});
   $("#login_form input[type=password]").keydown(function(e){if(e.which ==13) {	$("#loginBtn").click();}});
   
	
	$("#loginUserModel").click(function(){
		
		$("#myModal").modal({
            backdrop : "static",
            keyboard: true,
            remote:"/userlogin.jspx"
        });
		$('#myModal').modal({show:true,backdrop:false});  
		//alert("sdsd");
	});//
   
    $("#regeUserModel").click(function(){
		
		$("#myModal").modal({
            backdrop : "static",
            keyboard: true,
            remote:"/user_register.jspx"
        });
		$('#myModal').modal({show:true,backdrop:false});  
		//alert("sdsd");
	});
    
   $("#tucaoinfo").click(function(){
	
	   if($("#tucaoinfo").attr("che")!=null&&$("#tucaoinfo").attr("che")!=''){
		   $("#myModal").modal({
		         backdrop : "static",
		         keyboard: true,
		         remote:"/tucaoforward.jhtml"
		     });
		    $('#myModal').modal({show:true,backdrop:false}); 
	   }else{
		   bootbox.alert('请登陆后吐槽!');
	   }
	 
  });
   
   $("#userinfo").click(function(){
		
	   if($("#userinfo").attr("che")!=null&&$("#userinfo").attr("che")!=''){
		   $("#userinfo").attr("href","/member/index.jhtml");
		   $("#userinfo").attr("target","_blank");
		  
	   }else{
		   bootbox.alert('请登陆后查看个人信息!');
	   }
	 
  });
   
});
