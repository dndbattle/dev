/**
 * 
 */
package com.dndcombat.monsters.g;

import java.util.List;

import com.dndcombat.creatures.model.CreatureItemModel;
import com.dndcombat.fight.model.AttackResult;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.Damage;
import com.dndcombat.model.RefList;

/**
 
 */
public class GrayOoze extends Player {
 
	private static final long serialVersionUID = 1L;

	public GrayOoze() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Gray Ooze");
//   update creatures set active_ind = 1 where player_ind = 0 and active_ind = 0 and creature_name = 'Gray Ooze'; 
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Gray Ooze.webp");

	    str(12).dex(6).con(16).inte(1).wis(6).cha(2);

		// Saving Throws
		strSave(+1).dexSave(-2).conSave(+3).intSave(-5).wisSave(-2).chaSave(-4);
	
		 // Combat Stats
		ac(9).init(-2).hp(22).speed(10).cr(0.5);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesooze);

		damageResist(RefList.damagetypescold);
		damageResist(RefList.damagetypesfire);
		damageResist(RefList.damagetypesacid);
		conditionImmune(RefList.conditionsexhaustion);
		conditionImmune(RefList.conditionsfrightened);
		conditionImmune(RefList.conditionsrestrained);
		conditionImmune(RefList.conditionscharmed);
		conditionImmune(RefList.conditionsprone);
	 
        addAction(new Action("Pseudopod", RefList.actiontypesmainaction)
                .addAttack(Pseudopod()));
	}

    //TODO reduction of ac
	private Attack Pseudopod() {
	
        Attack a = new Attack("Pseudopod"); 
        a.setAttackBonus(3) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(8) 
         .bonus(1) 
         .damageTypeRefId(RefList.damagetypesacid); 
        a.addModifierType(new DefaultModifierType(a.getAttackName()) {
 
			private static final long serialVersionUID = 1L;
        	
			@Override
			public boolean applyDamageOnAttackHit(Player source, Action action, Attack attack, Player target, List<Damage> newdmglist, AttackResult attackResult, GameState state, List<Damage> damageSoFar) {
				for (CreatureItemModel cim : target.getCreature().getItems()) {
					if (cim.getEquippedInd() == 1) { 
						if (cim.getArmorClass() > 10) {
							if (cim.getMagicBonus() == 0) {
								state.addActionLog("Acid damage reduces " + target.getName() + " armor class by 1");
								cim.setArmorClass(cim.getArmorClass()-1);
							}
						}
					}
				}
				return false;
			}
        });
        return a;
    }

}

