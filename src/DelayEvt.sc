;;; Sierra Script 1.0 - (do not remove this comment)
(script# 947)
(include sci.sh)
(use User)
(use System)


(class DelayedEvent of Event
	(properties
		type $0000
		message 0
		modifiers $0000
		y 0
		x 0
		claimed 0
		client 0
	)
	
	(method (perform param1 param2 &tmp superPerform)
		(= superPerform (super perform:))
		(if argc
			(superPerform client: param1)
			(if (>= argc 2)
				(superPerform
					type: (param2 type?)
					message: (param2 message?)
					modifiers: (param2 modifiers?)
					y: (param2 y?)
					x: (param2 x?)
					claimed: (param2 claimed?)
				)
			else
				(GetEvent 32767 superPerform)
			)
		)
		(return superPerform)
	)
	
	(method (changeState)
		(if client (client indexOf: self))
		(self new:)
	)
)

(class GoToDlyEvt of DelayedEvent
	(properties
		type $0000
		message 0
		modifiers $0000
		y 0
		x 0
		claimed 0
		client 0
		polyTest 0
		freewayTest 0
	)
	
	(method (perform)
		(= polyTest (User input?))
		(= freewayTest (User alterEgo?))
		(User verbMessager: 0 getInput: 0)
		(super perform: &rest)
	)
	
	(method (changeState)
		(User verbMessager: polyTest getInput: freewayTest)
		(Parse (User prompt?) self)
		(super changeState: &rest)
	)
)
