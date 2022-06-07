export const utils = {
    isEmpty: function(val) {
        return typeof val !== 'string' || val === '';
    },
    getHashValue: function(){
        const hash = window.location.hash;
        if(!this.isEmpty(hash)) {
            return hash.substring(1);
        }
        return '';
    }
};

export default utils;