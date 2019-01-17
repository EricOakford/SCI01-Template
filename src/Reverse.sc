;;; Sierra Script 1.0 - (do not remove this comment)
(script# REVERSE)
(include system.sh) (include sci2.sh)
(use Motion)


(class Reverse of Cycle
	;;; Cycles client's cel constantly in reverse, wrapping to the last cel
	;;; in the client's loop when the cel goes below 0.
	(properties
		name "Rev"
		client 0
		caller 0
		cycleDir -1
		cycleCnt 0
		completed 0
	)
	
	(method (doit &tmp newCel)
		(if (< (= newCel (self nextCel:)) 0)
			(self cycleDone:)
		else
			(client cel: newCel)
		)
	)
	
	(method (cycleDone)
		;; When 'done', reset to last cel and keep going.
		(client cel: (client lastCel:))
	)
)
