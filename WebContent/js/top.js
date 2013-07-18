$(function(){
            var top_name = 'dest';
            var top_aligndiv_name = 'qq-group';


            var top_pic = '<div id="'+top_name+'" style=" cursor:pointer; z-index:2; width:48px; height:48px; position: fixed !important; bottom: 15%;" ><img src="img/top.jpg" /></div>';
            $("#content").append(top_pic);

            $("#dest").css({"opacity":"0"});
            winL = $("#dest").offset().left;

            $(window).scroll(function(){                          
            var winH = $(window).scrollTop();
            var winW= $("."+top_aligndiv_name).offset().left+125-$(window).scrollLeft();
            /* = $(window).width()-$(window).scrollLeft()-1/3*$(window).width();*/
            $("#dest").css({"left":(winW)+"px","cursor":"pointer"});
            /*var controly=$(window).scrollTop() + 320 - $("#dest").height()-100
            $("#dest").css({"top":(controly)+"px"});
            */if(winH>20){$("#dest").stop().animate({opacity:1},500);}
            else if(winH<=20)
            {$("#dest").stop().animate({opacity:0},500,function(){$(this).css("cursor","auto");}
            );}
            })

            $("#dest").click(function(){
            $( 'html, body' ).animate({scrollTop:0},300);
            /*$("#dest").css("top",(dest-400)+"px");*/
            })
        });