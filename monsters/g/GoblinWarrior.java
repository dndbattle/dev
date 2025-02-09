/**
 * 
 */
package com.dndcombat.monsters.g;

import java.util.List;

import com.dndcombat.fight.model.AttackResult;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.ModifierType;
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
public class GoblinWarrior extends Player {
 
	private static final long serialVersionUID = 1L;

	public GoblinWarrior() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Goblin Warrior");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Goblin.webp");

	    str(8).dex(15).con(10).inte(10).wis(8).cha(8);

		// Saving Throws
		strSave(-1).dexSave(+2).conSave(+0).intSave(+0).wisSave(-1).chaSave(-1);
	
		 // Combat Stats
		ac(15).init(+2).hp(10).speed(30).cr(0.25);

        // Size and Type
	    size(RefList.creaturesizessmall).type(RefList.creaturetypesgoblinoid);


        addAction(new Action("Scimitar", RefList.actiontypesmainaction)
                .addAttack(Scimitar()));
       addAction(new Action("Shortbow", RefList.actiontypesmainaction)
                .addAttack(Shortbow()));

       getCreature().addModifierType(damageOnAdv());
	}

    public static ModifierType damageOnAdv() {
    	return new DefaultModifierType("Advantage") {
 
			private static final long serialVersionUID = 1L;
    		@Override
    		public boolean applyDamageOnAttackHit(Player source, Action action, Attack attack, Player target, List<Damage> list, AttackResult attackResult, GameState state, List<Damage> damageSoFar) {
    			if (attackResult.overallAdvantage()) {
    				int total = Roll.d(4);
    				state.addActionLog("Advantage adds " + total);
    				list.add(new Damage(total, attack.getDamage().get(0).getDamageTypeRefId()));
    			}
    			return false;
    		}
    	};
    }

	private Attack Scimitar() {
	
        Attack a = new Attack("Scimitar"); 
        a.setAttackBonus(4) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(2) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }
    private Attack Shortbow() {
        Attack a = new Attack("Shortbow"); 
        a.setAttackBonus(4) 
         .setAttackRange(80) 
         .setLongRange(320) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(2) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        // If you wish to store thrown range, do something like:
        // a.setThrownRange(80, 320); 
        return a;
    }

}

