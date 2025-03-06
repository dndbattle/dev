/**
 * 
 */
package com.dndcombat.monsters.g;

import com.dndcombat.actions.MovementManager;
import com.dndcombat.actions.TargetManager;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.model.TargetActionPair;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.traits.MagicResistance;

/**
 
 */
public class Goristro extends Player {
 
	private static final long serialVersionUID = 1L;

	public Goristro() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Goristro");
//   update creatures set active_ind = 1 where player_ind = 0 and active_ind = 0 and creature_name = 'Goristro'; 
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Goristro.webp");

	    str(25).dex(11).con(25).inte(6).wis(13).cha(14);

		// Saving Throws
		strSave(+13).dexSave(+6).conSave(+13).intSave(-2).wisSave(+7).chaSave(+2);
	
		 // Combat Stats
		ac(19).init(+6).hp(310).speed(50).cr(17);

        // Size and Type
	    size(RefList.creaturesizeshuge).type(RefList.creaturetypesfiend);

		damageResist(RefList.damagetypescold);
		damageResist(RefList.damagetypesfire);
		damageResist(RefList.damagetypeslightning);
		damageImmune(RefList.damagetypespoison);
		conditionImmune(RefList.conditionspoisoned);
		getCreature().addModifierType(new MagicResistance());
 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( BrutalGore(), Slam(), Slam() ));


        addAction(new Action("Brutal Gore", RefList.actiontypesmainaction)
                .addAttack(BrutalGore()));

        addAction(new Action("Slam", RefList.actiontypesmainaction)
                .addAttack(Slam()));
        
        getCreature().addModifierType(new DefaultModifierType("Charge")  {
 
			private static final long serialVersionUID = 1L;
         	@Override
        	public void applyModifierTypeOnMeStartOfMyTurn(Player me, GameState state) {
        	 
        		Player target = new TargetManager().getNearestEnemy(me, state);
        		if (target != null && me.hasBonusAction() && !me.isNextTo(target)) {
        			me.useBonusAction();
        			TargetActionPair tap = new TargetActionPair(target, new Action("Charge",RefList.actiontypesfreeaction), 0);
        			int moveleft = me.getMovementLeft();
        			me.getStatus().setMovementLeft(me.getCurrentSpeed()/2);
        			new MovementManager().executeMovement(me, tap, true, state);
        			me.getStatus().setMovementLeft(moveleft); // reset
        			state.addActionLog(me.getName() + " uses charge");
        		}
        	}
        });
	}


	private Attack BrutalGore() {
	
        Attack a = new Attack("Brutal Gore"); 
        a.setAttackBonus(13) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage()
         .pushedAway(20)
         .autoApplyCondition(RefList.conditionsprone, new DurationType(10))
         .nbrDice(6) 
         .dieType(10) 
         .bonus(7) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        
        return a;
    }

	private Attack Slam() {
	
        Attack a = new Attack("Slam"); 
        a.setAttackBonus(13) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(4) 
         .dieType(10) 
         .bonus(7) 
         .damageTypeRefId(RefList.damagetypesbludgeoning); 
        
        return a;
    }

}

