function initTouch()
{
	addTouchListener("dpadUp");
	addTouchListener("dpadLeft");
	addTouchListener("dpadRight");
	addTouchListener("dpadDown");
	
	addTouchListener("face1");
	addTouchListener("face2");
	addTouchListener("face3");
	addTouchListener("face4");
}

function addTouchListener(id)
{
	var obj = document.getElementById(id);
	obj.addEventListener('touchstart', function(event) 
	{
		handleTouched(id, "down");
	}, false);
	
	obj.addEventListener('touchend', function(event) 
	{
		handleTouched(id, "up");
	}, false);
}

function handleTouched(id, type)
{
	var obj = document.getElementById(id);
	sendButtonEvent(id,type);
	obj.value = type;	
}

function sendButtonEvent(button, event)
{
	var myRand=parseInt(Math.random()*99999999);
	var url = document.URL + "/buttonEvent" + myRand+ "?button=" + button + "&event=" + event;
	
	var xmlhttp
	if (window.XMLHttpRequest)
	{
		xmlhttp=new XMLHttpRequest();
	}
	
	xmlhttp.open("GET",url,true);
	xmlhttp.send();
}