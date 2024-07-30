import { useEffect, useState } from 'react';
import axios from 'axios'
import Cookies from 'js-cookie';
import { useNavigate } from 'react-router-dom';

function Home(){
    const [currentDate, setCurrentDate] = useState("")
    const [name, setName] = useState("")
    const [readerData, setReaderData] = useState("")
    const [writerData, setWriterData] = useState("")
    const [selectedDate, setSelectedDate] = useState("");
    const [editLabel, setEditLabel] = useState("Edit");
    const [sectionBackground, setSectionBackground] = useState(`bg-gray-900`)
    const [postMode, setPostMode] = useState("W") // "write" OR "update" | currently set to "write"
    const [dateLabel, setDateLabel] = useState("Today")
    const [postLabel, setPostLabel] = useState("Post")
    const navigate = useNavigate();

    function handleSignOut(event){
        event.preventDefault();
        axios.post('http://localhost:8080/v1/auth/sign-out', {
            "token": Cookies.get('token')
            })
            .then(function (response) {
                Cookies.remove('token')
                navigate('/signin')
            })
            .catch(function (error) {
                if (error.response) {
                    Cookies.remove('token')
                    navigate('/signin')
                }
        });
    }

    function handleDeleteAccount(){
        if (confirm("Are you sure you want to delete the account?") === true) {
            axios.delete('http://localhost:8080/v1/auth/delete-account', {
                data: {
                    "token" : Cookies.get('token')
                }
                })
                .then(function (response) {
                    Cookies.remove('token')
                    alert("Account deleted!");
                    navigate("/signin")
                })
                .catch(function (error) {
                    if (error.response) {
                        alert(error.response.data.payload);
                    }
            });
        }
    }

    function wisher(name){
        const hours = new Date().getHours();
        if(hours < 12){
            return "Good morning, " + name + "!";
        }else if(hours < 16){
            return "Good afternoon, " + name + "!";
        }else{
            return "Good evening, " + name + "!";
        }
    }

    function cleanReader(){
        setReaderData("")
    }

    function cleanWriter(){
        setWriterData("")
    }

    function postJournal(){
        if(writerData == ""){
            if(postMode == "W"){
                alert("No journal found to post!")
            }else{
                alert("No journal found to update!")
            }
        }else{
            if(postMode == "W"){
                const journal = writerData.replace(/\n/g, "<>")
                axios.post('http://localhost:8080/v1/journal/save-journal', {
                    "token": Cookies.get('token'),
                    "date": currentDate,
                    "journal": journal
                })
                .then(function (response) {
                    alert("Journal saved!");
                })
                .catch(function (error) {
                    if (error.response) {
                        alert(error.response.data.payload);
                    }
                });
            }else{
                if(writerData === readerData){
                    alert("No changes are made to update.")
                }else{
                    const journal = writerData.replace(/\n/g, "<>")
                    axios.post('http://localhost:8080/v1/journal/update-journal', {
                        "token": Cookies.get('token'),
                        "date": currentDate,
                        "journal": journal
                    })
                    .then(function (response) {
                        alert("Journal updated!");
                        finishUpdate()
                    })
                    .catch(function (error) {
                        if (error.response) {
                            alert(error.response.data.payload);
                        }
                    });
                }

            }
        }
    }

    function handleSearch(){
        if(selectedDate === ""){
            alert("Date is not selected. Please select a date to search.")
        }else{
            axios.get('http://localhost:8080/v1/journal/get-journal', {
                params: {
                    token: Cookies.get('token'),
                    date: selectedDate
                }
            })
            .then(function (response) {
                setReaderData(response.data.payload.replace(/<>/g, "\n"))
            })
            .catch(function (error) {
                if (error.response) {
                    alert(error.response.data.payload);
                }
            });
        }
    }

    function handleEdit(){
        console.log(postMode)
        if(postMode === "U"){
            setWriterData("")
            setSectionBackground(`bg-gray-900`)
            setPostMode("W")
            setEditLabel("Edit")
            setDateLabel("Today")
            setCurrentDate
            setPostLabel("Post")
            alert("Editing canceled.")
        }else{
            if(readerData == ""){
                alert("No journal found to be edited.")
            }else{
                if(writerData != ""){
                    let response = confirm("Are you sure to overwrite the existing journal in the writing section?(Please save the existing journal before going for editing.)")
                    if(response == true){
                        prepareEdit()
                    }else{
                        alert("Editing canceled.")
                    }
                }else{
                    prepareEdit()
                }
            }
        }
    }

    function prepareEdit(){
        setWriterData(readerData)
        setSectionBackground(`bg-orange-950`)
        setEditLabel("Cancel")
        setPostMode("U") // set to "update"
        setDateLabel("Editing")
        setCurrentDate(selectedDate)
        setPostLabel("Update")
    }

    function finishUpdate(){
        setEditLabel("Edit")
        setPostLabel("Post")
        setDateLabel("Today")
        setSectionBackground(`bg-gray-900`)
        setPostMode("W")
        setWriterData("")
    }

    function handleDeleteJournal(){
        if(readerData === ""){
            alert("No journal present to be deleted.")
        }else{
            if (confirm("Are you sure you want to delete the journal?") === true) {
                axios.delete('http://localhost:8080/v1/journal/delete-journal', {
                    data: {
                        "token" : Cookies.get('token'),
                        "date" : selectedDate
                    }
                    })
                    .then(function (response) {
                        setReaderData("")
                        alert("Journal deleted!");
                    })
                    .catch(function (error) {
                        if (error.response) {
                            alert(error.response.data.payload);
                        }
                });
            }
        }
    }

    useEffect(() => {
        if(postMode == "W"){
            const date = new Date();
            let today = date.getFullYear() + "-" + 
                    (date.getMonth()+1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1) + "-" + 
                    (date.getDate()+1 < 10 ? "0" + date.getDate() : date.getDate());
            setCurrentDate(today)
        }
    })

    useEffect(() => {
        if(Cookies.get('token') === undefined){
            navigate('/signin')
        }else{
            axios.get('http://localhost:8080/v1/auth/get-name', {
                params: {
                    token: Cookies.get('token')
                }
                })
                .then(function (response) {
                    setName(wisher(response.data.payload));
                })
                .catch(function (error) {
                    if (error.response) {
                        alert(error.response.data.payload);
                    }
            });
        }
    }, [])

    return (
        <>
            <div className="h-screen w-screen p-5">
                <div className='bg-sky-700 w-full h-16 m-auto rounded-lg'>
                    <div className='text-sky-100 flex flex-row'>
                        <h1 className='text-4xl font-bold font-serif p-3'>Micro Journal</h1>
                        <p className="text-xl font-bold font-serif text-sky-100 mt-6 ms-5 basis-8/12">{name}</p>
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="size-8 my-auto ms-20 rounded-md cursor-pointer hover:bg-sky-900" onClick={handleSignOut}>
                            <path strokeLinecap="round" strokeLinejoin="round" d="M8.25 9V5.25A2.25 2.25 0 0 1 10.5 3h6a2.25 2.25 0 0 1 2.25 2.25v13.5A2.25 2.25 0 0 1 16.5 21h-6a2.25 2.25 0 0 1-2.25-2.25V15m-3 0-3-3m0 0 3-3m-3 3H15" />
                        </svg>
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="size-7 my-auto ms-7 rounded-md cursor-pointer hover:bg-red-700" onClick={handleDeleteAccount}>
                            <path strokeLinecap="round" strokeLinejoin="round" d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0" />
                        </svg>
                    </div>
                </div>
                <div className='bg-slate-700 rounded-xl float-left mt-8 ms-10 me-6 h-4/6 w-6/12 px-3'>
                    <input type="date" value={selectedDate} min="2024-06-01" max={currentDate} className="m-3 ms-32 bg-gray-900 text-sky-500 p-1 rounded-xl" onChange={(e) => setSelectedDate(e.target.value)} />
                    <input type="submit" value="Search" className="text-sky-600 font-bold text-xl mx-2 mt-4 hover:text-sky-500 cursor-pointer" onClick={handleSearch} />
                    <input type="submit" value={editLabel} className="text-orange-600 font-bold text-xl ms-16 me-3 mt-4 hover:text-orange-500 cursor-pointer" onClick={handleEdit} />
                    <input type="submit" value="Clear" className="text-gray-400 mt-4 font-bold text-xl mx-3  hover:text-gray-300 cursor-pointer" onClick={cleanReader} />
                    <input type="submit" value="Delete" className="text-red-700 mt-4 font-bold text-xl mx-3  hover:text-red-600 cursor-pointer" onClick={handleDeleteJournal} />
                    <textarea value={readerData} readOnly disabled className="h-5/6 w-full bg-gray-900 rounded-xl p-5 text-white resize-none" />
                </div>
                <div className='bg-slate-700 rounded-xl float-left mt-8 ms-6 h-4/6 w-5/12 px-3 pt-3'>
                    <textarea className={`h-5/6 w-full ${sectionBackground} rounded-xl p-5 text-white resize-none`} value={writerData} onChange={(e) => setWriterData(e.target.value)} />
                    <div className='flex flex-row'>
                        <p className='text-gray-300 mt-5 ms-2 font-bold w-fit basis-3/4'>{currentDate} ({dateLabel})</p>
                        <input type="submit" value="Clear" className="text-gray-400 mt-4 font-bold text-xl mx-3  hover:text-gray-300 cursor-pointer" onClick={cleanWriter} />
                        <input type="submit" value={postLabel} className="text-sky-600 mt-4 font-bold text-xl mx-3 hover:text-sky-500 cursor-pointer" onClick={postJournal} />
                    </div>
                </div>
            </div>
        </>
    )
}

export default Home;