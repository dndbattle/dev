/**
 * 
 */
package com.dndcombat.monsters.b;

import com.dndcombat.actions.InvalidActionException;
import com.dndcombat.fight.model.AdvantageModel;
import com.dndcombat.fight.model.AttackSettings;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.ModifierType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 
 */
public class BugbearWarrior extends Player {
 
	private static final long serialVersionUID = 1L;

	public BugbearWarrior() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Bugbear Warrior");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Bugbear.webp");

	    str(15).dex(14).con(13).inte(8).wis(11).cha(9);

		// Saving Throws
		strSave(+2).dexSave(+2).conSave(+1).intSave(-1).wisSave(+0).chaSave(-1);
	
		 // Combat Stats
		ac(14).init(+2).hp(33).speed(30).cr(1);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesgoblinoid);
	    
        addAction(new Action("Grab", RefList.actiontypesmainaction)
                .addAttack(Grab()));
        
        addAction(new Action("Light Hammer", RefList.actiontypesmainaction)
                .addAttack(LightHammer()));
        addAction(new Action("Light HammerThrown", RefList.actiontypesmainaction)
                .addAttack(LightHammerThrown()));

        getCreature().addModifierType(getAdv());
        
	}
    
    private ModifierType getAdv() {
    	return new DefaultModifierType("Adv if grappled") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public int evaluateScore(Player me, Player target, Action action, long actionTypeRefId, GameState state) throws InvalidActionException {
				if (me.isGrappling(target) && action.is("Grab")) {
					return -10;
				}
				return 0;
			}
    		@Override
    		public void modifierTypeOnSourceAlterAdvantage(AdvantageModel a, Player source, Player target, Action action, Attack attack, GameState state, AttackSettings settings) {
    			if (source.isGrappling(target) && action.is("Light Hammer")) {
    				a.setAdvantage(true);
    				a.setAdvantageReason("Grappled");
    			}
    		}
    	};
    }
    
	private Attack Grab() {
		
        Attack a = new Attack("Grab"); 
        a.setAttackBonus(4) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .setGrappleOnHit(true)
         .setGrappleDc(12)
         .setGrappleMediumOrSmaller(true)
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(6) 
         .bonus(2) 
         .damageTypeRefId(RefList.damagetypesbludgeoning); 
        
        return a;
    }

	


	private Attack LightHammer() {
	    Attack a = new Attack("Light Hammer"); 
	    a.setAttackBonus(4) 
	     .setMeleereach(10) // 0 if not found
	     .setRollToHitInd(1) 
	     .getFirstDamage() 
	     .nbrDice(3) 
	     .dieType(4) 
	     .bonus(2) 
	     .damageTypeRefId(RefList.damagetypesbludgeoning); 
	    // If you wish to store thrown range, do something like:
	    // a.setThrownRange(20, 60); 
	    return a;
	}

	
	private Attack LightHammerThrown() {
	    Attack a = new Attack("Light Hammer Thrown"); 
	    a.setAttackBonus(4) 
	     .setAttackRange(20) // 0 if not found
	     .setLongRange(60) // 0 if not found
	     .setRollToHitInd(1) 
	     .getFirstDamage() 
	     .nbrDice(3) 
	     .dieType(4) 
	     .bonus(2) 
	     .damageTypeRefId(RefList.damagetypesbludgeoning); 
	    // If you wish to store thrown range, do something like:
	    // a.setThrownRange(20, 60); 
	    return a;
	}


}

