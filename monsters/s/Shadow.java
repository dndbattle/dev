/**
 * 
 */
package com.dndcombat.monsters.s;

import java.util.List;

import com.dndcombat.fight.model.AttackResult;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.Damage;
import com.dndcombat.model.RefList;
import com.dndcombat.util.Roll;

/**
 
 */
public class Shadow extends Player {
 
	private static final long serialVersionUID = 1L;

	public Shadow() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Shadow");
//   
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Shadow.webp");

	    str(6).dex(14).con(13).inte(6).wis(10).cha(8);

		// Saving Throws
		strSave(-2).dexSave(+2).conSave(+1).intSave(-2).wisSave(+0).chaSave(-1);
	
		 // Combat Stats
		ac(12).init(+2).hp(27).speed(40).cr(0.5);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesundead);

		damageResist(RefList.damagetypescold);
		damageResist(RefList.damagetypesfire);
		damageResist(RefList.damagetypeslightning);
		damageResist(RefList.damagetypesthunder);
		damageResist(RefList.damagetypesacid);
		damageImmune(RefList.damagetypesnecrotic);
		damageImmune(RefList.damagetypespoison);
		conditionImmune(RefList.conditionsexhaustion);
		conditionImmune(RefList.conditionsfrightened);
		conditionImmune(RefList.conditionspoisoned);
		conditionImmune(RefList.conditionsrestrained);
		conditionImmune(RefList.conditionsparalyzed);
		conditionImmune(RefList.conditionspetrified);
 
        addAction(new Action("Draining Swipe", RefList.actiontypesmainaction)
                .addAttack(DrainingSwipe()));
	}


	private Attack DrainingSwipe() {
	
        Attack a = new Attack("Draining Swipe"); 
        a.setAttackBonus(4) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(2) 
         .damageTypeRefId(RefList.damagetypesnecrotic);
        a.addModifierType(new DefaultModifierType(a.getAttackName()) {
 
			private static final long serialVersionUID = 1L;
			@Override
			public boolean applyDamageOnAttackHit(Player source, Action action, Attack attack, Player target,
					List<Damage> newdmglist, AttackResult attackResult, GameState state, List<Damage> damageSoFar) {
				int amt =  Roll.d(4);
				state.addActionLog(target.getName() + " loses " + amt + " strength");
			 
				target.getCreature().setBaseStrength(target.getCreature().getBaseStrength() -amt);
				//TODO technically gets it back on a long rest
				return false;
			}
        	
        });
        
        return a;
    }

}

