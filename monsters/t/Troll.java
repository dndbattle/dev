/**
 * 
 */
package com.dndcombat.monsters.t;

import java.util.List;

import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.ModifierType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.Damage;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.traits.Regeneration;

/**
 
 */
public class Troll extends Player {
 
	private static final long serialVersionUID = 1L;

	public Troll() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Troll");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Troll.webp");

	    str(18).dex(13).con(20).inte(7).wis(9).cha(7);

		// Saving Throws
		strSave(+4).dexSave(+1).conSave(+5).intSave(-2).wisSave(-1).chaSave(-2);
	
		 // Combat Stats
		ac(15).init(+1).hp(94).speed(30).cr(5);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesgiant);


        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Rend(),Rend(),Rend() ));


        addAction(new Action("Rend", RefList.actiontypesmainaction)
                .addAttack(Rend()));
        
        
        getCreature().addModifierType(new Regeneration(15));
        getCreature().addModifierType(FireDamage());
	}
    
    //TODO Troll Limb
    //TODO charge
    
	private ModifierType FireDamage() {
		DefaultModifierType mt = new DefaultModifierType("Rampage()") {
 
		private static final long serialVersionUID = 1L;
		
		@Override
		public boolean reactToBeingDamagedTrueToRemove(Player me, Player attacker, int dmg, List<Damage> damage, Action action, Attack attack, GameState state) {
			for (Damage d : damage) {
				if (d.dmg > 0) {
					if (d.damageTypeRefId == RefList.damagetypesfire || d.damageTypeRefId == RefList.damagetypesacid) {
						me.markCast("RegenerationSkipped");
					}
				}
			}
			return false;
		}
		};
		
		return mt;
	}


	private Attack Rend() {
	
        Attack a = new Attack("Rend"); 
        a.setAttackBonus(7) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(6) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }

}

