$(function () {
    var scrtime;

    var $ul = $("#con ul");
    var liFirstHeight = $ul.find("li:first").height();//��һ��li�ĸ߶�
    $ul.css({ top: "-" + liFirstHeight - 20 + "px" });//����css��top���Խ���һ��li�������б��Ϸ�	 ��li������padding:10px����Ҫ-20

    $("#con").hover(function () {
        $ul.pause();//��ͣ����
        clearInterval(scrtime);
    }, function () {
    	//alert("1");
        $ul.resume();//�ָ����Ŷ���	
        scrtime = setInterval(function scrolllist() {
            //������ʽչ�ֵ�һ��li
            $ul.animate({ top: 0 + "px" }, 1500, function () {
            	 
            	 ain_func();
                				
            });
        }, 3300);

    }).trigger("mouseleave");//ͨ��trigger("mouseleave")����������hover�¼��ĵ�2������

});

