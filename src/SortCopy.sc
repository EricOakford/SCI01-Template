;;; Sierra Script 1.0 - (do not remove this comment)
(script# 984)
(include game.sh)
(use Main)
(use Sight)
(use System)

(public
	SortedAdd 0
)

(local
	frontList
	backList
	outList
)
(procedure (SortedAdd &tmp newEventHandler_4 newEventHandler_5 newEventHandler_6)
	((= frontList (EventHandler new:))
		add:
		name: {fl}
	)
	((= outList (EventHandler new:))
		add:
		name: {ol}
	)
	((= backList (EventHandler new:))
		add:
		name: {bl}
	)
	((= newEventHandler_4 (EventHandler new:)) name: {fl2})
	((= newEventHandler_5 (EventHandler new:)) name: {ol2})
	((= newEventHandler_6 (EventHandler new:)) name: {bl2})
	(cast eachElementDo: #perform preSortCode)
	(features eachElementDo: #perform preSortCode)
	(Sort frontList newEventHandler_4 frontSortCode)
	(sortedFeatures add: newEventHandler_4)
	(Sort outList newEventHandler_5 frontSortCode)
	(sortedFeatures add: newEventHandler_5)
	(sortedFeatures add: regions)
	(sortedFeatures add: locales)
	(Sort backList newEventHandler_6 backSortCode)
	(sortedFeatures add: newEventHandler_6)
	(frontList release: dispose:)
	(outList release: dispose:)
	(backList release: dispose:)
)

(instance preSortCode of Code
	(properties)
	
	(method (doit param1)
		(cond 
			((CantBeSeen param1 ego) (backList add: param1))
			((IsOffScreen param1) (outList add: param1))
			(else (frontList add: param1))
		)
	)
)

(instance frontSortCode of Code
	(properties)
	
	(method (doit param1 &tmp temp0 temp1)
		(= temp0 (ego distanceTo: param1))
		(= temp1
			(AngleDiff
				(ego heading?)
				(GetAngle (ego x?) (ego y?) (param1 x?) (param1 y?))
			)
		)
		(if (== (umod temp1 180) 0) (-- temp1))
		(if
		(< (= temp0 (Abs (CosDiv (/ temp1 2) temp0))) 0)
			(= temp0 32767)
		)
		(return temp0)
	)
)

(instance backSortCode of Code
	(properties)
	
	(method (doit param1 &tmp temp0 temp1)
		(= temp0 (ego distanceTo: param1))
		(= temp1
			(AngleDiff
				(ego heading?)
				(GetAngle (ego x?) (ego y?) (param1 x?) (param1 y?))
			)
		)
		(if (== (umod temp1 90) 0) (-- temp1))
		(if (< (= temp0 (SinDiv temp1 temp0)) 0)
			(= temp0 32767)
		)
	)
)
