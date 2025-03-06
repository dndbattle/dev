/**
 * 
 */
package com.dndcombat.monsters.s;
 
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

/**
 
 */
public class Specter extends Player {
 
	private static final long serialVersionUID = 1L;

	public Specter() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Specter");
//   
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Specter.webp");

	    str(1).dex(14).con(11).inte(10).wis(10).cha(11);

		// Saving Throws
		strSave(-5).dexSave(+2).conSave(+0).intSave(+0).wisSave(+0).chaSave(+0);
	
		 // Combat Stats
		ac(12).init(+2).hp(22).speed(30).flySpeed(50).cr(1);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesundead);

		damageResist(RefList.damagetypescold);
		damageResist(RefList.damagetypesfire);
		damageResist(RefList.damagetypeslightning);
		damageResist(RefList.damagetypesthunder);
		damageResist(RefList.damagetypesslashing);
		damageResist(RefList.damagetypespiercing);
		damageResist(RefList.damagetypesbludgeoning);
		damageResist(RefList.damagetypesacid);
		damageImmune(RefList.damagetypesnecrotic);
		damageImmune(RefList.damagetypespoison);
		conditionImmune(RefList.conditionsexhaustion);
		conditionImmune(RefList.conditionspoisoned);
		conditionImmune(RefList.conditionsrestrained);
		conditionImmune(RefList.conditionscharmed);
		conditionImmune(RefList.conditionsparalyzed);
		conditionImmune(RefList.conditionspetrified);
		conditionImmune(RefList.conditionsprone);
		
		getCreature().getActions().add(new Action("Life Drain", RefList.actiontypesmainaction).addAttack(LifeDrain()));
		  getCreature().addModifierType(drain());
		}
		    
		    private ModifierType drain() {

			  DefaultModifierType mt = new DefaultModifierType("Bite") {
		        	 
					private static final long serialVersionUID = 1L;
					@Override
					public boolean applyDamageOnAttackHit(Player source, Action action, Attack attack, Player target, List<Damage> list, AttackResult attackResult, GameState state, List<Damage> damageSoFar) {
		     		    
	            		int total = 0;
	            		for (Damage d : damageSoFar) {
	            			if (d.damageTypeRefId == RefList.damagetypesnecrotic) {
	            				total += d.dmg;
	            			}
	            		}
	            		if (total > 0) {
	            		    state.addActionLog(source.getName() + " drains " + total);
	            		    target.reduceMaxHp(total, state);
	               		    state.addActionLog(target.getName() + " max hp reduced to " + target.getStatus().getMaxHitPoints());
	               		 
	            		}
	           	 
		        		return false;
		        	}
		        };
		        return mt;
		    }

    private Attack LifeDrain() {
        Attack a = new Attack("Life Drain");
        a.setAttackBonus(4) // +4 to hit
         .setMeleereach(5) // Reach of 5 feet
         .setRollToHitInd(1) // Roll to hit
         .getFirstDamage()
         .nbrDice(2) // 2d6
         .dieType(6)
         .damageTypeRefId(RefList.damagetypesnecrotic);
    
        return a;
    }

}

