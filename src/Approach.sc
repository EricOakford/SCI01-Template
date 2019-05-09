;;; Sierra Script 1.0 - (do not remove this comment)
(script# APPROACH)
(include game.sh)
(use Motion)


(class Approach of Motion
	;; Try to get near an immobile object.
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
		who 0             ;who to approach
		distance 20       ;how close to 'who' is considered a 'near'

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
	)
	
	(method (doit)
		(if (self onTarget:)
			(self moveDone:)
		else
			(super doit:)
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
