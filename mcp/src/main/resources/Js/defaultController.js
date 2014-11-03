

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

var savedId = null;

function getId()
{
	var prmstr = window.location.search.substr(1);
	var prmarr = prmstr.split ("&");
	var params = {};

	for ( var i = 0; i < prmarr.length; i++) {
	    var tmparr = prmarr[i].split("=");
	    params[tmparr[0]] = tmparr[1];
	}
	
	if(params.id != null)
	{
		return params.id;
	}
	else
	{
		if(savedId == null)
		{
			var xmlhttp
			if (window.XMLHttpRequest)
			{
				xmlhttp=new XMLHttpRequest();
			}
			
			xmlhttp.open("GET","getIpAddress",false);
			xmlhttp.send();
			
			var obj = JSON.parse(xmlhttp.responseText);
			var IP = obj.ip;
			
			var tmparr2 = IP.split(".");
			
			savedId = tmparr2[3];
		}
		
		return savedId;
	}
}

function getUrl()
{
	pathArray = window.location.href.split( '/' );
	protocol = pathArray[0];
	host = pathArray[2];
	return protocol + '://' + host;
}

function sendButtonEvent(button, event)
{
	var myRand=parseInt(Math.random()*99999999);
	var url = getUrl() + "/buttonEvent" + myRand+ "?button=" + button + "&event=" + event + "&id=" + getId();
	
	var xmlhttp
	if (window.XMLHttpRequest)
	{
		xmlhttp=new XMLHttpRequest();
	}
	
	xmlhttp.open("GET",url,true);
	xmlhttp.send();
}

function sendAnalogStickEvent(analog, x, y)
{
	var myRand=parseInt(Math.random()*99999999);
	var url = getUrl() + "/analogEvent" + myRand+ "?analog=" + analog + "&x=" + x + "&y=" + y + "&id=" + getId();
	
	var xmlhttp
	if (window.XMLHttpRequest)
	{
		xmlhttp=new XMLHttpRequest();
	}
	
	xmlhttp.open("GET",url,true);
	xmlhttp.send();
}

function sendOrientationEvent(gamma, beta, alpha)
{
	var myRand=parseInt(Math.random()*99999999);
	var url = getUrl() + "/orientationEvent" + myRand+ "?gamma=" + gamma + "&beta=" + beta + "&alpha=" + alpha + "&id=" + getId();
	
	var xmlhttp
	if (window.XMLHttpRequest)
	{
		xmlhttp=new XMLHttpRequest();
	}
	
	xmlhttp.open("GET",url,true);
	xmlhttp.send();
}