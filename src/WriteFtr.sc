;;; Sierra Script 1.0 - (do not remove this comment)
(script# WRITEFTR)
(include game.sh)
(use Main)
(use Intrface)
(use Feature)
(use File)
(use Actor)
(use System)


(local
	[nameString 100]
	[nounString 100]
	[sCString 100]
	[oCString 100]
	[descString 190]
	toScreen =  TRUE
	drawNSRect =  TRUE
	inited = FALSE
	theType
)

(procedure (DoFeature obj &tmp event theX theY theTop theLeft theBottom theRight)
	(Print 948 10)
	(while (!= ((= event (Event new:)) type?) mouseDown)
		(event dispose:)
	)
	(GlobalToLocal event)
	(= theTop (event y?))
	(= theLeft (event x?))
	(Print 948 11)
	(while (!= ((= event (Event new:)) type?) mouseDown)
		(event dispose:)
	)
	(GlobalToLocal event)
	(= theBottom (event y?))
	(= theX
		(+
			(/ (- (= theRight (event x?)) theLeft) 2)
			theLeft
		)
	)
	(= theY (+ (/ (- theBottom theTop) 2) theTop))
	(obj
		x: theX
		y: theY
		nsLeft: theLeft
		nsTop: theTop
		nsBottom: theBottom
		nsRight: theRight
	)
	(if drawNSRect
		(Graph GDrawLine theTop theLeft theTop theRight VMAP vWHITE)
		(Graph GDrawLine theBottom theLeft theBottom theRight VMAP vWHITE)
		(Graph GDrawLine theTop theLeft theBottom theLeft VMAP vWHITE)
		(Graph GDrawLine theTop theRight theBottom theRight VMAP vWHITE)
		(Graph GShowBits theTop theLeft theBottom theRight VMAP)
	)
	(event dispose:)
)

(procedure (DoView obj &tmp event)
	(obj
		view: (GetNumber {View?} 0)
		loop: (GetNumber {Loop?} 0)
		cel: (GetNumber {Cel?} 0)
		signal: 16400
		priority: 15
		init:
	)
	(if (obj respondsTo: #illegalBits)
		(obj illegalBits: 0)
	)
	(while (!= ((= event (Event new:)) type?) 1)
		(GlobalToLocal event)
		(obj posn: (event x?) (event y?))
		(Animate (cast elements?) FALSE)
		(event dispose:)
	)
	(event dispose:)
)

(procedure (LogIt what)
	(File name: @sysLogPath writeString: what close:)
	(DisposeScript FILE)
)

(procedure (DoProperties obj)
	(obj
		noun: (GetInput @nounString 30 {/noun?})
		shiftClick: (DoDoVerb @sCString 20 {shftClick verb?})
		contClick: (DoDoVerb @oCString 20 {cntrlClick verb?})
		sightAngle: (GetNumber {sight angle?} 90)
		closeRangeDist: (GetNumber {getable dist?} 50)
		longRangeDist: (GetNumber {seeable dist?} 100)
		description: (GetInput @descString 50 {description?})
	)
)

(procedure (DoDoVerb param1 param2 param3)
	(GetInput param1 param2 param3)
	(return
		(cond 
			((StrCmp param1 {verbLook}) verbLook)
			((StrCmp param1 {verbOpen}) verbOpen)
			((StrCmp param1 {verbClose}) verbClose)
			((StrCmp param1 {verbSmell}) verbSmell)
			((StrCmp param1 {verbMove}) verbMove)
			((StrCmp param1 {verbEat}) verbEat)
			((StrCmp param1 {verbGet}) verbGet)
		)
	)
)

(class WriteCode
	(properties)
	
	(method (doit theObj &tmp [buffer 400] [vlcOrNsStr 40])
		(if (theObj isMemberOf: Feature)
			(Format
				@vlcOrNsStr
				948
				0
				(theObj nsLeft?)
				(theObj nsTop?)
				(theObj nsBottom?)
				(theObj nsRight?)
			)
		else
			(Format
				@vlcOrNsStr
				948
				1
				(theObj view?)
				(theObj loop?)
				(theObj cel?)
			)
		)
		(Format
			@buffer
			948
			2
			@nameString
			(if (== theType 2)
				(PicView name?)
			else
				((theObj superClass?) name?)
			)
			(theObj x?)
			(theObj y?)
			(theObj z?)
			(theObj heading?)
			@nounString
			@vlcOrNsStr
			@descString
			(theObj sightAngle?)
			(theObj closeRangeDist?)
			(theObj longRangeDist?)
			@sCString
			@oCString
		)
		(if toScreen
			(Print @buffer #font 999 #title {Feature Writer V1.0})
		)
		(LogIt @buffer)
		(if (theObj isMemberOf: Feature)
			(theObj dispose:)
		else
			(theObj addToPic:)
		)
	)
	
	(method (writeList theList)
		(theList eachElementDo: #perform self)
		(CreateObject doit:)
	)
)

(class CreateObject
	(properties)
	
	(method (doit &tmp [thePath 15] theObj event)
		(if (not inited)
			(= thePath 0)
			(GetInput @thePath 30 {Enter path and filename})
			(Format @sysLogPath @thePath)
			(Format @sCString 948 3)
			(Format @oCString 948 4)
			(switch
				(Print
					948
					5
					#title
					{Feature Writer V1.0}
					#button
					{YES}
					1
					#button
					{NO}
					2
				)
				(1 (= drawNSRect 1))
				(2 (= drawNSRect 0))
			)
			(switch
				(Print
					948
					6
					#title
					{Feature Writer V1.0}
					#button
					{YES}
					1
					#button
					{NO}
					2
				)
				(1 (= toScreen 1))
				(2 (= toScreen 0))
			)
			(= inited 1)
		)
		(if
			(not
				(= theType
					(Print
						948
						7
						#title
						{Feature Writer V1.0}
						#button
						{Feature}
						1
						#button
						{PicView}
						2
						#button
						{View}
						3
						#button
						{Prop}
						4
						#button
						{Actor}
						5
					)
				)
			)
			(return)
		)
		(= theObj
			(
			(switch theType
				(1 Feature)
				(2 View)
				(3 View)
				(4 Prop)
				(5 Actor)
			)
				new:
			)
		)
		(GetInput @nameString 30 {Name?})
		(StrCpy @descString @nameString)
		(DoProperties theObj)
		(if (== theType 1)
			(DoFeature theObj)
		else
			(DoView theObj)
		)
		(if
			(Print
				948
				8
				#title
				{Feature Writer V1.0}
				#button
				{YES}
				1
				#button
				{NO}
				0
			)
			(Print 948 9)
			(while (!= ((= event (Event new:)) type?) 1)
				(event dispose:)
			)
			(GlobalToLocal event)
			(theObj z: (- (event y?) (theObj y?)))
			(theObj y: (event y?))
			(event dispose:)
		)
		(WriteCode doit: theObj)
	)
)
