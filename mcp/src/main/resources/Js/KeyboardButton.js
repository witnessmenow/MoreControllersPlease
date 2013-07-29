function KeyboardButton(name, keyCode)
{
	this.name = name;
	this.keyCode = keyCode;
	
	//Holding the keys seems to send repeat commands, this should stop that
	this.lastSent = "";
};

KeyboardButton.prototype.checkForButtonDown = function(pressedKeyCode)
{
	this.handleEvent(pressedKeyCode, "down");
}

KeyboardButton.prototype.checkForButtonUp = function(pressedKeyCode)
{
	this.handleEvent(pressedKeyCode, "up");
}

KeyboardButton.prototype.checkForButtonPressed = function(pressedKeyCode)
{
	this.handleEvent(pressedKeyCode, "pressed");
}

KeyboardButton.prototype.handleEvent = function(pressedKeyCode, action)
{
	if(this.keyCode == pressedKeyCode)
	{
		if(this.lastSent != action)
		{
			sendButtonEvent(this.name, action);
			this.lastSent = action;
		}
	}
}
