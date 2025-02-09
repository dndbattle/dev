/**
 * 
 */
package com.dndcombat.monsters.g;

import java.util.List;

import com.dndcombat.actions.ActionManager;
import com.dndcombat.fight.model.AttackResult;
import com.dndcombat.fight.model.AttackSettings;
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
public class GnollWarrior extends Player {
 
	private static final long serialVersionUID = 1L;

	public GnollWarrior() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Gnoll Warrior");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Gnoll.webp");

	    str(14).dex(12).con(11).inte(6).wis(10).cha(7);

		// Saving Throws
		strSave(+2).dexSave(+1).conSave(+0).intSave(-2).wisSave(+0).chaSave(-2);
	
		 // Combat Stats
		ac(15).init(+1).hp(27).speed(30).cr(0.5);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesfiend);


        addAction(new Action("Rend", RefList.actiontypesmainaction)
                .addAttack(Rend()));
       addAction(new Action("Bone Bow", RefList.actiontypesmainaction)
                .addAttack(BoneBow()));

       getCreature().addModifierType(rampage());
	}


	private Attack Rend() {
	
        Attack a = new Attack("Rend"); 
        a.setAttackBonus(4) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(2) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        
        return a;
    }
    private Attack BoneBow() {
        Attack a = new Attack("Bone Bow"); 
        a.setAttackBonus(3) 
         .setAttackRange(150) 
         .setLongRange(600) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(10) 
         .bonus(1) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        // If you wish to store thrown range, do something like:
        // a.setThrownRange(150, 600); 
        return a;
    }
    
    private ModifierType rampage() {
		DefaultModifierType mt = new DefaultModifierType("Rampage") {
 
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean applyDamageOnAttackHit(Player source, Action action, Attack attack, Player target, List<Damage> list, AttackResult attackResult, GameState state, List<Damage> damageSoFar) {
				if (target.getPercentHealth() <= 50) {
					Action rampage = new Action("Rampage", RefList.actiontypesfreeaction);
					rampage.addAttack(Rend());
					new ActionManager().rollAttack(source, rampage, rampage.getFirstAttack(), target, state, new AttackSettings());
					state.flagThisFight();
				}
				return false;
			}
		};
		
		return mt;
	}

}

