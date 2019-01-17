;;; Sierra Script 1.0 - (do not remove this comment)
(script# CHASE)
(include system.sh) (include sci2.sh)
(use Motion)


(class Chase of Motion
	(properties
		client 0
		caller 0
		x 0
		y 0
		dx 0
		dy 0
		b-moveCnt 0
		b-i1 0
		b-i2 0
		b-di 0
		b-xAxis 0
		b-incr 0
		completed 0
		xLast 0
		yLast 0
		who 0			;who to chase
		distance 0		;how close to 'who' is considered a 'catch'

	)
	
	(method (init actor whom howClose whoCares)
		(if (>= argc 1)
			(= client actor)
			(if (>= argc 2)
				(= who whom)
				(if (>= argc 3)
					(= distance howClose)
					(if (>= argc 4) (= caller whoCares))
				)
			)
		)
		(super init: client (who x?) (who y?) caller)
		;(super doit:)
	)
	
	(method (doit)
		(if (self onTarget:)
			(self moveDone:)
		else
			(super doit:)
			(if (== b-moveCnt 0)
				(super init: client (who x?) (who y?) caller)
			)
		)
	)
	
	(method (setTarget)
		(cond 
			(argc (super setTarget: &rest))
			((not (self onTarget:)) (super setTarget: (who x?) (who y?)))
		)
	)
	
	(method (onTarget)
		(return (<= (client distanceTo: who) distance))
	)
)
