import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom'
import Cookies from 'js-cookie';
import { useNavigate } from 'react-router-dom';
import axios from 'axios'

function SignIn(){
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [validationError, setValidationError] = useState("")
    const navigate = useNavigate();

    function handleSubmit(event){
        event.preventDefault();
        let errorMessage = validate(email, password)
        if(errorMessage == "OK"){
            setValidationError("")
            axios.post('http://localhost:8080/v1/auth/sign-in', {
                email: email,
                password: password
              })
              .then(function (response) {
                Cookies.set('token', response.data.payload,  { expires: 365 })
                navigate('/home')
              })
              .catch(function (error) {
                if (error.response) {
                    alert(error.response.data.payload);
                }
              });
        }else{
            setValidationError(errorMessage)
        }
    }

    function validate(email, password){
        if(email == "" && password == ""){
            return "Email and password fields are empty!"
        }else if(email == ""){
            return "Please enter the email."
        }else if(password == ""){
            return "Please enter the password."
        }
        if(!(email.includes("@")) || !(email.includes("."))){
            return "Oops! Given email is not an email!"
        }else if(password.length < 8){
            return "Oops! Password length is less than 8!"
        }
        return "OK"
    }

    useEffect(() => {
        if(Cookies.get('token') !== undefined){
            navigate('/home')
        }
    })

    return (
        <>
            <div className="w-screen h-screen pt-36">
                <div className="float-left h-4/5 w-1/2 pt-20">
                    <h1 className="text-center px-20 py-5 font-serif text-6xl font-extrabold text-sky-300">Micro Journal</h1>
                    <h5 className="text-center font-serif text-xl font-semibold text-blue-100">Your Life, One Micro Note at a Time</h5>
                </div>
                <div className="float-left h-4/5 w-1/2">
                    <div className="w-1/2 h-2/3 p-10 m-auto mt-5 bg-slate-700 rounded-xl">
                        <h1 className="font-serif text-3xl font-extrabold text-white">Sign In</h1>
                        <div className="w-full h-8">
                            <h6 className="text-orange-500">{validationError}</h6>
                        </div>
                        <form onSubmit={handleSubmit}>
                            <input className="w-full p-2 rounded-md" placeholder="Enter email here" type="text" onChange={(e) => setEmail(e.target.value)} />
                            <input className="w-full mt-4 p-2 rounded-md" placeholder="Enter password here" type="password" onChange={(e) => setPassword(e.target.value)} />
                            <input className="mt-5 bg-sky-700 p-2 mb-20 rounded-md hover:bg-sky-600 text-white font-bold cursor-pointer" type="submit" value="Sign In" /> 
                        </form>
                    </div>
                    <Link to="/signup"><h2 className="text-sky-500 text-center text-lg hover:underline hover:text-sky-300 cursor-pointer m-5">New user? click here to Sign up!</h2></Link>
                </div>
            </div>
        </>
    )
}

export default SignIn;