function StickHelper(mainCanvas, id)
{
	this.id = id;
	
	this.windowWidth = window.innerWidth;
	this.windowHeight = window.innerHeight;

	this.canvas = mainCanvas;
	this.context = this.canvas.getContext("2d");

	this.stickImage = new Image();
	this.stickImage.src = "Analog_stick_guide_by_Eric_the_Rexman.png";
	this.stickImage.onload = function () {
		setInterval( function() {processFunction(this);}, 1);
	};
	
	this.limitSize = this.windowWidth*(0.15);
	this.knobSize = this.limitSize/2;
	this.inputSize = this.knobSize;
	this.lastTime = Date.now();

	this.limitX = this.windowWidth*(0.2);
	this.limitY = this.windowHeight*(0.5);
	this.limitXAdjusted = this.limitX - (this.limitSize / 2);
	this.limitYAdjusted = this.limitY - (this.limitSize / 2);

	this.clearWidth =  this.windowWidth*(0.4);
	
	this.stick = new Stick(this.inputSize, false);
	
	this.stick.limit = {
		x: this.limitX,
		y: this.limitY
	};
	
	this.stick.input = {
		x: this.limitX,
		y: this.limitY
	};
	
	this.timeSinceLastUpdate = 0;
	this.lastStickNormalXSent = 0;
	this.lastStickNormalYSent = 0;
};

function processFunction(stickHelper)
{
	stickHelper.processStick();
}

StickHelper.prototype.draw = function() 
{
	this.context.clearRect(0, 0, this.windowWidth*(0.4), this.windowHeight);
	this.drawStick();
};

StickHelper.prototype.drawStick = function() 
{
	this.context.save();

	var knobSz = this.knobSize;
	
	// Limit
	this.context.drawImage(
		this.stickImage,
		0, 0,
		89, 88,
		this.limitXAdjusted, this.limitYAdjusted,
		this.limitSize, this.limitSize
	);

	// Input
	this.context.drawImage(
		this.stickImage,
		89, 14,
		61, 60,
		this.stick.input.x - (knobSz / 2) , this.stick.input.y - (knobSz / 2),
		knobSz, knobSz
	);

	this.context.restore();
};

StickHelper.prototype.handleTouchStart = function(e)
{
	e.preventDefault();

	if(this.stick.active == false)
	{
		for (var i = 0; i < e.changedTouches.length; ++i) 
		{
			var touch = e.changedTouches[i];

			if(touch.pageX <  this.clearWidth)
			{
				this.stick.setInputXY(touch.pageX, touch.pageY);
				this.stick.active = true;
				this.stick.identifier = touch.identifier;
			}
		}
	}
};

StickHelper.prototype.handleTouchMove = function(e)
{
	console.log('Stick Helper - Handle Move');

	//e.preventDefault();
	if (this.stick.active)
	{
		console.log('stick is active');
	
		for (var i = 0; i < e.changedTouches .length; ++i) 
		{
			console.log('in for loop');
			var touch = e.changedTouches[i];
			console.log('Stick ident=' +this.stick.identifier+ " touch ident=" + touch.identifier);
			if (this.stick.identifier == touch.identifier)
			{
				this.stick.setInputXY(touch.pageX, touch.pageY);
			}
		}
	}
};

StickHelper.prototype.handleTouchEnd = function(e)
{
	//e.preventDefault();
	
	var touches = e.changedTouches;
	if (this.stick.active)
	{
		for (var i = 0; i < touches.length; ++i) 
		{
			var touch = touches[i];
		
			if (this.stick.identifier == touch.identifier)
			{
				this.stick.active = false;
				this.stick.setInputXY(this.stick.limit.x, this.stick.limit.y);
			}
		}
	}
};

StickHelper.prototype.processStick = function() {
	var now = Date.now();
	var elapsed = (now - this.lastTime);

	this.update(elapsed);
	this.draw();

	this.lastTime = now;
};

function roundStickValue(value)
{
	var retVal = value * 100;
	retVal = Math.round(retVal);
	retVal = retVal/100;
	return retVal;
}

StickHelper.prototype.sendUpdateToServer = function(){

	var xVal = roundStickValue(this.stick.normal.x);
	var yVal = roundStickValue(this.stick.normal.y);

	sendAnalogStickEvent(this.id, xVal, yVal);
	this.lastStickNormalXSent = this.stick.normal.x;
	this.lastStickNormalYSent = this.stick.normal.y;
	this.timeSinceLastUpdate = 100;
}

StickHelper.prototype.update = function(elapsed){
	this.stick.update();
	
	if(this.timeSinceLastUpdate <= 0)
	{
		if(this.stick.normal.x != this.lastStickNormalXSent || this.stick.normal.y != this.lastStickNormalYSent)
		{
			//Send the event
			this.sendUpdateToServer();
		}
	}
	else
	{
		this.timeSinceLastUpdate -= elapsed;
	}
};

