function(doc){
    if(doc.type === 'Location'){
        emit([doc.country, doc.state, doc.city], doc);
    }
}