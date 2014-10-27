$(document).ready(function(){
	var guide_type=$('body').attr('data');
	if(guide_type=='home'){
		$.ajax({
			url:'/app/user/needGuidance',
			dataType:'json',
			type:'GET',
			success:function(data){	
				if(JSON.stringify(data)=='\"true\"'){
					Guidance(guide_type);
				}
				
			}
		});	
	}
	else if(guide_type=='profile'){
		$.ajax({
			url:'/app/user/needGuidance/'+USERID,
			dataType:'json',
			type:'GET',
			success:function(data){
				if(JSON.stringify(data)=='\"true\"'){
					Guidance(guide_type);
				}
				
			}
		});	
	}
	
});

function Guidance(data){
	if(data=='home'){
		$('.guide_next_btn').click(function(event){
			var data=parseInt($(event.target).attr('data')); 
			$('.guide_step[data='+data.toString()+']').fadeOut(300,function(){
				if(data==$('#guide_mask').attr('content-image-data')){
					$('#guide_mask').fadeOut(500);
					$('html,body').animate({scrollTop:0},500);
				} else{
					data++;
					$('.guide_step[data='+data.toString()+']').fadeIn(300);
					$('html,body').animate({scrollTop:$('.guide_step[data='+data.toString()+']').offset().top},300);
				}
			});
			
		}); 

		$('.guide_exit_btn').click(function(event){
			var data=parseInt($(event.target).attr('data')); 
			$('.guide_step[data='+data.toString()+']').fadeOut(300,function(){
				$('#guide_mask').fadeOut(500);
				$('html,body').animate({scrollTop:0},500);
			});
		});
		$(window).resize(function(){
			$('.guide_step[data=1]').css({'top':$('.activityHome').offset().top-5,'left':$('.activityHome').offset().left-15});
			$('.guide_step[data=2]').css({'top':$('.communityHome').offset().top-45,'left':$('.communityHome').offset().left-55});
			$('.guide_step[data=3]').css({'top':$('.circleHome').offset().top-140,'left':$('.circleHome').offset().left-100});
			$('.guide_step[data=4]').css({'top':$('.peopleHome').offset().top-165,'left':$('.peopleHome').offset().left-90});
			$('.guide_step[data=5]').css({'top':$('.QA').offset().top-93,'left':$('.QA').offset().left-100});
			$('.guide_step[data=6]').css({'top':$('.btnFind').offset().top-493,'left':$('.btnFind').offset().left-581});
		});
		$(window).resize();
		$('.mask').css({height:document.body.scrollHeight}).delay(1000).fadeIn(500,function(){
			$('.guide_step[data=1]').delay(300).fadeIn(500);
		});
	}
	else if (data=="profile"){
		
		$('.guide_step[data=3]').attr("data-toggle", "modal");
		$('.guide_step[data=3]').attr("data-target", "#avatar-modal");
		$('#avatar-modal').on('show', function(){
			console.log('show');
		});
		$('#avatar-modal').on('hidden', function(){
			console.log('hidden');
		});
		
		$('.guide_next_btn').click(function(event){
			var data=parseInt($(event.target).attr('data')); 
			$('.guide_step[data='+data.toString()+']').fadeOut(300,function(){
				if(data==$('#guide_mask').attr('content-image-data')){
					$('#guide_mask').fadeOut(500);
					$('html,body').animate({scrollTop:0},500);
				} else{
					if(data==3){
						

					}
					
					
					data++;
					$('.guide_step[data='+data.toString()+']').fadeIn(300);
					$('html,body').animate({scrollTop:$('.guide_step[data='+data.toString()+']').offset().top},300);
				}
			});
			
		}); 

		$('.guide_exit_btn').click(function(event){
			var data=parseInt($(event.target).attr('data')); 
			$('.guide_step[data='+data.toString()+']').fadeOut(300,function(){
				$('#guide_mask').fadeOut(500);
				$('html,body').animate({scrollTop:0},500);
			});
		});
		$(window).resize(function(){
			$('.guide_step[data=1]').css({'top':'0','left':'20%'});
			$('.guide_step[data=2]').css({'top':$('.glyphicon-home').offset().top-10,'left':$('.glyphicon-home').offset().left-610});
			$('.guide_step[data=3]').css({'top':$('.profile_container').offset().top-5,'left':$('.profile_container').offset().left+55});
			$('.guide_step[data=4]').css({'top':$('.aboutGreen').offset().top-100,'left':$('.aboutGreen').offset().left+5});
		});
		$(window).resize();
		$('.mask').css({height:document.body.scrollHeight+200}).delay(1000).fadeIn(500,function(){
			$('.guide_step[data=1]').delay(300).fadeIn(500);
		});
	}
	
}








