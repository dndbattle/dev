package com.dndcombat.monsters.r;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 * Redbrand Ruffian class representing a medium humanoid enemy.
 */
public class RedbrandRuffian extends Player {

    private static final long serialVersionUID = 1L;

    public RedbrandRuffian() {
        super(new Creature());
        create();
    }

    /**
     * Initializes the Redbrand Ruffian's attributes, stats, and actions.
     */
    public void create() {
        getCreature().setCreatureName("Redbrand Ruffian");
        setPlayersSide(false);
        getCreature().setImageUrl("/assets/monsters/LMoP/Redbrand Ruffian.webp");

        // Ability Scores
        str(11).dex(14).con(12).inte(9).wis(9).cha(11);

        // Saving Throws
        strSave(0).dexSave(2).conSave(1).intSave(-1).wisSave(-1).chaSave(0);

        // Combat Stats
        ac(14).init(2).hp(16).speed(30).cr(0.5);

        // Size and Type
        size(RefList.creaturesizesmedium).type(RefList.creaturetypeshumanoid);
 
        // Actions
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Shortsword(), Shortsword()));

        addAction(new Action("Shortsword", RefList.actiontypesmainaction)
                .addAttack(Shortsword()));
    }

    /**
     * Creates the Shortsword attack.
     *
     * @return The Attack object representing the Shortsword attack.
     */
    private Attack Shortsword() {
        Attack a = new Attack("Shortsword");
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
}