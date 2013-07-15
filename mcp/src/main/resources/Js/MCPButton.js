function MCPButton(mainCanvas, id, buttonImageUrl, x, y, buttonWidth, buttonHeight)
{
	this.windowWidth = window.innerWidth;
	this.windowHeight = window.innerHeight;

	this.canvas = mainCanvas;
	this.context = this.canvas.getContext("2d");
	
	this.buttonX = x;
	this.buttonY = y;
	this.buttonXAdjusted = x - buttonWidth/2;
	this.buttonYAdjusted = y - buttonHeight/2;
	
	this.buttonWidth = buttonWidth;
	this.buttonHeight = buttonHeight;
	
	this.buttonImage = null;
	
	this.buttonImageUrl = buttonImageUrl;
	this.buttonImageId = id;
	
	this.pressedButtonImage = new Image();
	this.pressedButtonImage.src = "Pressed/" + buttonImageUrl;
	
	this.buttonDown = false;
	
	this.identifier = -1;
};

function setButtonImage(MCPButton)
{
	MCPButton.buttonImage = new Image();
	MCPButton.buttonImage.src = MCPButton.buttonImageUrl;
	MCPButton.buttonImage.id = MCPButton.buttonImageId;
	MCPButton.buttonImage.onload = function () {
		drawImageToCanvas(MCPButton);
	};
}

function drawImageToCanvas(MCPButton)
{
	MCPButton.drawButtonToCanvas(MCPButton.buttonImage);
};

MCPButton.prototype.drawButtonToCanvas = function(image)
{
	this.context.drawImage(image, this.buttonXAdjusted, this.buttonYAdjusted, this.buttonWidth, this.buttonHeight);
} 

MCPButton.prototype.setButtonDown = function(ident)
{
	this.buttonDown = true;
	this.drawButtonToCanvas(this.pressedButtonImage);
	this.identifier = ident;
	//Send Event to Server
	sendButtonEvent(this.buttonImageId, "down");
}

MCPButton.prototype.setButtonUp = function()
{
	this.buttonDown = false;
	this.drawButtonToCanvas(this.buttonImage);
	
	//Send Event to Server
	sendButtonEvent(this.buttonImageId, "up");
}

MCPButton.prototype.handleTouchStart = function(e)
{
	//e.preventDefault();
	
	var touches = e.changedTouches;
	for (var i = 0; i < touches.length; ++i) 
	{
		var touch = touches[i];
		
		if (this.checkCoOrds(touch.pageX, touch.pageY))
		{
			this.setButtonDown(touch.identifier);
		}
	}
}

MCPButton.prototype.handleTouchMove = function(e)
{
	console.log('MCPButton - Handle Move');

	e.preventDefault();
	var touches = e.changedTouches;
	
	for (var i = 0; i < touches.length; ++i) 
	{
		var touch = touches[i];
		if (this.checkCoOrds(touch.pageX, touch.pageY))
		{
			if(this.buttonDown)
			{
				//Do nothing, server already knows about the button press
			}
			else
			{
				//User has slid their finger over the button
				this.setButtonDown(touch.identifier);
			}
		}
		else
		{
			if(this.buttonDown && touch.identifier == this.identifier)
			{
				//We had an active press, but now the figer is out of range
				this.setButtonUp();
			}
		}
	}
};

MCPButton.prototype.handleTouchEnd = function(e)
{
	//e.preventDefault();
	
	var touches = e.changedTouches;
	for (var i = 0; i < touches.length; ++i) 
	{
		var touch = touches[i];
		if (this.checkCoOrds(touch.pageX, touch.pageY))
		{
			if(this.buttonDown && touch.identifier == this.identifier)
			{
				this.setButtonUp();
			}
		}
	}
};


MCPButton.prototype.checkCoOrds = function(x,y)
{
	if(x >= this.buttonXAdjusted && x <= (this.buttonXAdjusted + this.buttonWidth))
	{
		//its in the correct area on the X plane
		
		if(y >= this.buttonYAdjusted && y <= (this.buttonYAdjusted + this.buttonHeight))
		{
			return true;
		}
	}
	
	return false;
	
}