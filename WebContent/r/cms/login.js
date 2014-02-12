

function apply(obj){
        // alert(obj);
         if('${user!}'!=null&&'${user!}'!=''){
            $.ajax( {
              url : "/member/applyJson.jhtml",
              type: "post",
              data:{
               "u": obj,
             },
             dataType:"json",
             
            success:function (response) {
                // alert(response.message);  
            	 bootbox.alert(response.message);            
            }
           });               
         }else{
            bootbox.alert('请登陆后加好友!');
         }
     }
    

    if("${qqlogin!}"!=null&&"${qqlogin!}"=='1'){
       $("#myModal").modal({
            backdrop : "static",
            keyboard: true,
            remote:"${base}/qquser_bind.jspx"
        });
       $('#myModal').modal({show:true,backdrop:false});
    }
   
     if("${sinalogin!}"!=null&&"${sinalogin!}"=='1'){
       $("#myModal").modal({
            backdrop : "static",
            keyboard: true,
            remote:"${base}/sinauser_bind.jspx"
        });
   	$('#myModal').modal({show:true,backdrop:false});
    } 