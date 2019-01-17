;;; Sierra Script 1.0 - (do not remove this comment)
(script# DCICON)
(include system.sh) (include sci2.sh)
(use Intrface)
(use Motion)


(class DCIcon of DIcon
	;;; Cycling Icons are a sub-class of DIcon.
   ;;; An instance of DCIcon may be passed to Print for use in
   ;;; a dialog.

   ;;; additional properties are required to 
   ;;;allow cycling via the Cycler classes.
	(properties
		type $0004
		state $0000
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		key 0
		said 0
		value 0
		view 0
		loop 0
		cel 0
		cycler 0		; a cycler must be attached dynamically
		cycleSpeed 6	; 60ths second between cels. 
		signal 0		; just to satisfy cycler

	)

	;;; Do not pass a caller to this cycler
	(method (init)
		((= cycler (Forward new:)) init: self)
	)

	;;; A completion type cycler may have already disposed of itself	
	(method (dispose)
		(if cycler (cycler dispose:)
			)
		(super dispose:)
	)

	;;; invoked at 60 times per second by the Dialog doit: loop	
	(method (cycle &tmp oldCel)
		(if cycler
			;; remember current cel
			(= oldCel cel)
			(cycler doit:)
			
			;;; show new cel if it changed
			(if (!= cel oldCel)
				(self draw:)
				)
		)
	)

	;; Return the number of the last cel in the current loop of this object.
	;; this method is called by cycler	
	(method (lastCel)
		(return (- (NumCels self) 1))
	)
)
