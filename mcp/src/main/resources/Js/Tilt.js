function Tilt(roundValues)
{
	this.roundValues = roundValues;
	
	//Tilt Left-Right
	this.gamma = 0;
	
	//Tilt front-back
	this.beta = 0;
	
	//Compass Direction
	this.alpha = 0;
	
}

Tilt.prototype.sendInfo = function(eventData)
{
	if(this.roundValues)
	{
		this.gamma = Math.round(eventData.gamma);
		this.beta = Math.round(eventData.beta);
		this.alpha = Math.round(eventData.alpha);
	}
	else
	{
		this.gamma = eventData.gamma;
		this.beta = eventData.beta;
		this.alpha = eventData.alpha;
	}
		
	sendOrientationEvent(this.gamma, this.beta, this.alpha);
}